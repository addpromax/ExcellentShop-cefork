package su.nightexpress.nexshop.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Reflex;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;

/**
 * 用于解码旧版 Base32 压缩的 NBT 数据
 * 这是一次性迁移工具，转换完成后可以移除
 */
public class LegacyNbtDecoder {

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    
    private static final Class<?> CLS_CRAFT_ITEM_STACK = Reflex.getNMSClass(CRAFTBUKKIT_PACKAGE + ".inventory", "CraftItemStack");
    private static final Class<?> CLS_MINECRAFT_ITEM_STACK = Reflex.getNMSClass("net.minecraft.world.item", "ItemStack");
    private static final Class<?> CLS_COMPOUND_TAG = Reflex.getNMSClass("net.minecraft.nbt", "CompoundTag", "NBTTagCompound");
    private static final Class<?> CLS_NBT_IO = Reflex.getNMSClass("net.minecraft.nbt", "NbtIo", "NBTCompressedStreamTools");
    
    private static final Method M_NBT_IO_READ = Reflex.getMethod(CLS_NBT_IO, "read", "a", DataInput.class);
    private static final Method M_CRAFT_ITEM_STACK_AS_BUKKIT_COPY = Reflex.getMethod(CLS_CRAFT_ITEM_STACK, "asBukkitCopy", CLS_MINECRAFT_ITEM_STACK);
    
    /**
     * 从旧版 Base32 压缩的 NBT 字符串解码 ItemStack
     * 
     * @param compressed Base32 编码的压缩 NBT 字符串
     * @param sourceVersion 源数据版本（用于 DataFixer）
     * @return 解码后的 ItemStack，如果失败则返回 null
     */
    @Nullable
    public static ItemStack decodeFromBase32(@NotNull String compressed, int sourceVersion) {
        if (compressed.isBlank()) return null;
        
        try {
            // 步骤1: Base32 → 字节数组
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(compressed, 32).toByteArray());
            DataInputStream dataInput = new DataInputStream(inputStream);
            
            // 步骤2: 字节数组 → NBT CompoundTag (二进制读取，不经过字符串解析)
            Object compoundTag = Reflex.invokeMethod(M_NBT_IO_READ, null, dataInput);
            if (compoundTag == null) return null;
            
            // 步骤3: 使用新版 NbtUtil 转换 (包含 DataFixer)
            return su.nightexpress.nightcore.util.nbt.NbtUtil.tagToItemStack(compoundTag, sourceVersion);
        } catch (Exception e) {
            return null;
        }
    }
}
