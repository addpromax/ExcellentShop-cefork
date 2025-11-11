package su.nightexpress.nexshop.product.content.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.exception.ProductLoadException;
import su.nightexpress.nexshop.product.content.ContentType;
import su.nightexpress.nexshop.product.content.ProductContent;
import su.nightexpress.nexshop.util.ErrorHandler;
import su.nightexpress.nexshop.util.ShopUtils;
import su.nightexpress.nexshop.util.UnitUtils;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.impl.AdaptedCustomStack;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;
import su.nightexpress.nightcore.integration.item.impl.AdaptedVanillaStack;
import su.nightexpress.nightcore.util.ItemTag;
import su.nightexpress.nexshop.util.LegacyNbtDecoder;

public class ItemContent extends ProductContent {

    private final AdaptedItem adaptedItem;

    private boolean compareNbt;

    public ItemContent(@NotNull AdaptedItem adaptedItem, boolean compareNbt) {
        super(ContentType.ITEM);
        this.adaptedItem = adaptedItem;
        this.setCompareNbt(compareNbt);
    }

    @Nullable
    public static ItemContent read(@NotNull FileConfig config, @NotNull String path) throws ProductLoadException {
        updateVanillaConfig(config, path);
        updatePluginConfig(config, path);

        AdaptedItem adaptedItem = null;
        try {
            adaptedItem = AdaptedItemStack.read(config, path + ".Item");
        } catch (Exception e) {
            // 读取失败，可能是旧 NBT 格式导致的解析错误
            // 尝试自动修复
            if (tryAutoFixItem(config, path)) {
                // 修复成功，重新读取
                try {
                    adaptedItem = AdaptedItemStack.read(config, path + ".Item");
                } catch (Exception ex) {
                    ErrorHandler.configError("Auto-fix failed. Please recreate this item. Error: " + ex.getMessage(), config, path);
                    config.remove(path + ".Item");
                    return null;
                }
            } else {
                ErrorHandler.configError("Failed to load item. The NBT format may be corrupted or outdated. Please recreate this item. Error: " + e.getMessage(), config, path);
                config.remove(path + ".Item");
                return null;
            }
        }
        
        if (adaptedItem == null) return null;

        // 重新检测物品的真实adapter
        // 修复CE等自定义物品被错误识别为vanilla的问题
        ItemStack itemStack = adaptedItem.getItemStack();
        if (itemStack != null && adaptedItem.getAdapter().isVanilla()) {
            ItemAdapter<?> realAdapter = ItemBridge.getAdapter(itemStack);
            if (realAdapter != null && !realAdapter.isVanilla()) {
                // 物品实际上是自定义物品（如CE），使用正确的adapter重新适配
                AdaptedItem newAdaptedItem = realAdapter.adapt(itemStack).orElse(null);
                if (newAdaptedItem != null && newAdaptedItem.isValid()) {
                    adaptedItem = newAdaptedItem;
                    // 更新配置文件，保存正确的adapter信息
                    config.set(path + ".Item", newAdaptedItem);
                }
            }
        }

        boolean compareNbt = config.getBoolean(path + ".Item.CompareNBT");

        return new ItemContent(adaptedItem, compareNbt);
    }

    private static boolean tryAutoFixItem(@NotNull FileConfig config, @NotNull String path) {
        try {
            // 尝试读取已迁移但包含旧 NBT 数据的配置
            String provider = config.getString(path + ".Item.Provider");
            if (!"vanilla".equals(provider)) {
                return false; // 只处理原版物品
            }
            
            String oldNbtValue = config.getString(path + ".Item.Data.Value");
            if (oldNbtValue == null || oldNbtValue.isEmpty()) {
                return false;
            }
            
            // 检测是否是旧格式 Base32 字符串（不包含 { } 符号）
            if (oldNbtValue.contains("{") && oldNbtValue.contains("}")) {
                return false; // 已经是新格式 NBT 字符串
            }
            
            System.out.println("[ExcellentShop] 检测到旧格式 NBT，尝试自动转换: " + path);
            System.out.println("[ExcellentShop] 旧 NBT 值前 50 字符: " + oldNbtValue.substring(0, Math.min(50, oldNbtValue.length())));
            
            int dataVersion = config.getInt(path + ".Item.Data.DataVersion", -1);
            System.out.println("[ExcellentShop] 数据版本: " + dataVersion);
            
            // 尝试用二进制解码器解析旧格式
            ItemStack itemStack = LegacyNbtDecoder.decodeFromBase32(oldNbtValue, dataVersion);
            
            if (itemStack != null) {
                System.out.println("[ExcellentShop] 成功解码！物品类型: " + itemStack.getType());
                
                // 成功解析！用新格式重新编码
                ItemTag newTag = ItemTag.of(itemStack);
                System.out.println("[ExcellentShop] 新 NBT 标签前 50 字符: " + newTag.getTag().substring(0, Math.min(50, newTag.getTag().length())));
                
                AdaptedVanillaStack vanillaStack = new AdaptedVanillaStack(newTag);
                
                boolean compareNbt = config.getBoolean(path + ".Item.CompareNBT");
                config.set(path + ".Item", vanillaStack);
                config.set(path + ".Item.CompareNBT", compareNbt);
                config.saveChanges();
                
                System.out.println("[ExcellentShop] ✓ 成功转换并保存: " + path);
                return true;
            } else {
                System.out.println("[ExcellentShop] ✗ 解码失败，返回 null");
            }
        } catch (Exception e) {
            System.out.println("[ExcellentShop] ✗ 自动修复异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static void updateVanillaConfig(@NotNull FileConfig config, @NotNull String path) {
        if (config.contains(path + ".Content.Item")) {
            String oldTagString = String.valueOf(config.getString(path + ".Content.Item"));

            try {
                // 使用二进制解码器处理旧格式 Base32 压缩的 NBT
                ItemStack itemStack = LegacyNbtDecoder.decodeFromBase32(oldTagString, -1);
                
                if (itemStack != null) {
                    // 成功解析！使用当前版本重新编码
                    ItemTag newTag = ItemTag.of(itemStack);
                    config.remove(path + ".Content.Item");
                    config.set(path + ".ItemTag", newTag);
                    // 迁移成功
                } else {
                    ErrorHandler.configError("Could not decode old item format. Please recreate this item.", config, path);
                    config.remove(path + ".Content.Item");
                }
            } catch (Exception e) {
                ErrorHandler.configError("Failed to migrate old NBT format for item. Please recreate this item. Error: " + e.getMessage(), config, path);
                config.remove(path + ".Content.Item");
            }
        }

        if (config.contains(path + ".ItemTag")) {
            try {
                ItemTag tag = ItemTag.read(config, path + ".ItemTag");
                boolean respectMeta = config.getBoolean(path + ".Item_Meta_Enabled");

                // 验证并可能重新编码
                ItemStack itemStack = tag.getItemStack();
                if (itemStack != null) {
                    // 使用当前版本重新编码以确保格式正确
                    ItemTag updatedTag = ItemTag.of(itemStack);
                    
                    config.remove(path + ".ItemTag");
                    config.remove(path + ".Item_Meta_Enabled");

                    AdaptedVanillaStack vanillaStack = new AdaptedVanillaStack(updatedTag);
                    if (!vanillaStack.isValid()) {
                        ErrorHandler.configError("Migrated item is invalid. Please recreate this item.", config, path);
                        return;
                    }
                    
                    config.set(path + ".Item", vanillaStack);
                    config.set(path + ".Item.CompareNBT", respectMeta);
                    // ItemTag 更新成功
                } else {
                    ErrorHandler.configError("ItemTag could not be converted to ItemStack. Please recreate this item.", config, path);
                    config.remove(path + ".ItemTag");
                    config.remove(path + ".Item_Meta_Enabled");
                }
            } catch (Exception e) {
                ErrorHandler.configError("Failed to migrate ItemTag format. Please recreate this item. Error: " + e.getMessage(), config, path);
                config.remove(path + ".ItemTag");
                config.remove(path + ".Item_Meta_Enabled");
            }
        }
    }

    private static void updatePluginConfig(@NotNull FileConfig config, @NotNull String path) {
        if (!config.contains(path + ".Handler")) return;

        String handlerId = config.getString(path + ".Handler", "dummy");
        if (handlerId.equalsIgnoreCase("bukkit_item")) {
            config.remove(path + ".Handler");
            return;
        }

        ItemAdapter<?> adapter = ItemBridge.getAdapter(handlerId);
        if (!(adapter instanceof IdentifiableItemAdapter identifiableAdapter)) {
            ErrorHandler.configError("Invalid item handler '" + handlerId + "'.", config, path);
            return;
        }

        String itemId = config.getString(path + ".Content.ItemId", "null");
        int amount = config.getInt(path + ".Content.Amount", 1);

        AdaptedCustomStack customStack = new AdaptedCustomStack(identifiableAdapter, new ItemIdData(itemId, amount));
        config.set(path + ".Item", customStack);
        config.remove(path + ".Handler");
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Item", this.adaptedItem);
        config.set(path + ".Item.CompareNBT", this.compareNbt);
    }

    @NotNull
    public ItemStack getPreview() {
        return this.getItem();
    }

    @Override
    public void delivery(@NotNull Inventory inventory, int count) {
        ShopUtils.addItem(inventory, this.getItem(), UnitUtils.unitsToAmount(this.getUnitAmount(), count));
    }

    @Override
    public void take(@NotNull Inventory inventory, int count) {
        ShopUtils.takeItem(inventory, this::isItemMatches, UnitUtils.unitsToAmount(this.getUnitAmount(), count));
    }

    @Override
    public int count(@NotNull Inventory inventory) {
        return ShopUtils.countItem(inventory, this::isItemMatches);
    }

    @Override
    public boolean hasSpace(@NotNull Inventory inventory) {
        return this.countSpace(inventory) > 0;
    }

    @Override
    public int countSpace(@NotNull Inventory inventory) {
        return ShopUtils.countItemSpace(inventory, this::isItemMatches, this.getItem().getMaxStackSize());
    }

    @Override
    public int getUnitAmount() {
        return this.adaptedItem.getAmount();
    }

    public boolean isItemMatches(@NotNull ItemStack other) {
        if (!this.isValid()) return false;

        if (this.adaptedItem.getAdapter().isVanilla()) {
            ItemStack itemStack = this.getItem();
            return this.compareNbt ? itemStack.isSimilar(other) : itemStack.getType() == other.getType();
        }

        return this.adaptedItem.isSimilar(other);
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack itemStack = this.adaptedItem.getItemStack();
        if (itemStack == null) throw new IllegalStateException("Could not produce ItemStack from the AdaptedItem. Check #isValid before calling this method.");

        return itemStack;
    }

    @Override
    @NotNull
    public String getName() {
        return this.adaptedItem.getAdapter().getName();
    }

    @Override
    public boolean isValid() {
        return this.adaptedItem.isValid();
    }

    @NotNull
    public AdaptedItem getAdaptedItem() {
        return this.adaptedItem;
    }

    public boolean isCompareNbt() {
        return compareNbt;
    }

    public void setCompareNbt(boolean compareNbt) {
        this.compareNbt = compareNbt;
    }
}
