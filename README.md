# 关于本项目
![](https://nightexpressdev.com/excellentshop/banner.png)

<div align="center">

<a href="https://discord.gg/EwNFGsnGaW"><img src="https://nightexpressdev.com/img/overview/btn_discord.png"></a>&nbsp;
<a href="https://ko-fi.com/nightexpress"><img src="https://nightexpressdev.com/img/overview/btn_donate.png"></a>&nbsp;
<a href="https://nightexpressdev.com/excellentshop/"><img src="https://nightexpressdev.com/img/overview/btn_manual.png"></a>

**ExcellentShop** 是一个轻量级且现代化的三合一商店插件。<br>
包含 **GUI 商店**（支持轮换）、**箱子商店** 和 **拍卖行**。

</div>

---

## 🎯 Fork 版本特性

**版本**: 4.22.0-Fork

本 Fork 版本在原版基础上添加了以下改进：

### ✨ 完整的自定义物品插件集成

通过 NightCore 框架，现在 ExcellentShop 完全支持所有主流自定义物品插件：

- ✅ **ItemsAdder** - 完全支持自定义物品/方块
- ✅ **Nexo** - 完全支持自定义物品
- ✅ **Oraxen** - 完全支持自定义物品
- ✅ **MMOItems** - 完全支持 RPG 物品系统
- ✅ **ExecutableItems** - 完全支持可执行物品
- ✅ **CraftEngine** - 完全支持自定义物品/方块/家具（新增）
- ✅ **ExcellentCrates** - 完全支持箱子和钥匙

**主要优势**：
- 🔄 **自动识别和注册** - 插件启动时自动检测并注册适配器
- 📝 **统一配置格式** - 所有插件使用相同的简洁配置
- ⚡ **API 驱动** - 使用官方 API 而非 NBT 快照，自动跟随物品更新
- 🎨 **即插即用** - 无需任何手动配置

**详细文档**: 查看 [CUSTOM_ITEMS_INTEGRATION.md](CUSTOM_ITEMS_INTEGRATION.md) 了解完整使用说明

### 🔧 技术改进

- 重构了物品集成系统，统一通过 NightCore ItemBridge 管理
- 优化了物品识别和验证流程
- 改进了配置文件的简洁性和可维护性

---

## 功能特性

- **数据库支持** - 支持 SQLite 和 MySQL！
- **数据同步** - 跨多个服务器同步商品数据！
- **模块化设计** - 完全禁用您不喜欢的任何插件部分！
- [**多货币系统**](https://nightexpressdev.com/excellentshop//features/multi-currency) - 支持 [CoinsEngine](https://spigotmc.org/resources/84121/)、[PlayerPoints](https://www.spigotmc.org/resources/80745/)、[BeastTokens](https://www.spigotmc.org/resources/20806/) 等！
- [**自定义物品支持**](https://nightexpressdev.com/excellentshop/hooks/items) - 支持 [ItemsAdder](https://www.spigotmc.org/resources/73355/)、[Nexo](https://mcmodels.net/products/13172/nexo)、[Oraxen](https://www.spigotmc.org/resources/72448/)、[ExecutableItems](https://www.spigotmc.org/resources/83070/)、[MMOItems](https://www.spigotmc.org/resources/39267/)、**CraftEngine** 等！
- [**4种价格类型**](https://nightexpressdev.com/excellentshop/features/price-types) - 在虚拟商店和箱子商店中使用不同的价格系统！
- [**购买 GUI**](https://nightexpressdev.com/excellentshop/features/purchase-gui) - 简单、直观且完全可自定义的物品买卖界面！
- [**虚拟商店**](https://nightexpressdev.com/excellentshop/virtual/overview) - 创建完全可自定义的管理员 GUI 商店，提供大量选项！
  - **游戏内编辑器** - 通过简单直观的 GUI 直接在游戏中创建和管理商店！
  - [**主 GUI**](https://nightexpressdev.com/excellentshop/virtual/main-gui) - 通过完全可自定义的主商店 GUI 快速访问商店！
  - [**库存与限制**](https://nightexpressdev.com/excellentshop/virtual/stocks-limits) - 限制全局和每个玩家可购买和出售的商品数量！
  - [**物品与命令**](https://nightexpressdev.com/excellentshop/virtual/product-types) - 出售带有完整 NBT 支持的物品、自定义物品插件集成，以及支持 PlaceholderAPI 的命令！
  - [**商品要求**](https://nightexpressdev.com/excellentshop/virtual/product-requirements) - 为商店物品设置等级和权限要求！
  - [**商店布局**](https://nightexpressdev.com/excellentshop/virtual/shop-layouts) - 为商店创建完全自定义的 GUI 配置，支持每页布局！
  - [**商店要求**](https://nightexpressdev.com/excellentshop/virtual/shop-requirements) - 基于特定条件限制访问所有或特定商店！
  - [**商店轮换**](https://nightexpressdev.com/excellentshop/virtual/shop-rotations) - 在商店中创建随时间变化的动态优惠！
  - [**商店快捷方式**](https://nightexpressdev.com/excellentshop/virtual/shop-shortcuts) - 创建自定义命令快速访问商店！
  - [**出售功能**](https://nightexpressdev.com/excellentshop/virtual/sell-features) - 使用 GUI 快速出售整个物品栏、手持物品或特定物品！
  - [**出售倍数**](https://nightexpressdev.com/excellentshop/virtual/sell-multipliers) - 根据玩家的等级或权限提升出售价格！
- [**箱子商店**](https://nightexpressdev.com/excellentshop/chest/overview) - 在世界中的任何容器方块上创建箱子商店！
  - **游戏内编辑器** - 通过简单直观的 GUI 创建和管理商店！
  - **全息显示** - 使用基于数据包的客户端全息图显示商店信息！
  - **商店银行** - 通过银行功能分离您的口袋和商店余额！
  - **商店列表** - 在 GUI 中浏览您自己或其他玩家的商店！
  - **商店搜索** - 搜索包含特定物品的商店！
  - **商店数量** - 根据玩家的等级或权限设置可创建的商店数量！
  - **商店方块** - 通过放置特殊商店方块创建商店！
  - **商品数量** - 根据玩家的等级或权限设置每个商店可创建的商品数量！
  - **费用** - 向玩家收取创建和移除商店的费用！
  - [**管理员商店**](https://nightexpressdev.com/excellentshop/chest/admin-shops) - 将箱子商店设为拥有无限库存和金钱的管理员商店！
  - [**商店租赁**](https://nightexpressdev.com/excellentshop/chest/shop-renting) - 以商店主人设定的价格和时间租赁其他玩家创建的商店！
  - [**商店类型**](https://nightexpressdev.com/excellentshop/chest/shop-types) - 设置哪些方块允许成为箱子商店！包括 **潜影盒**、**桶** 等！
  - [**领地集成**](https://nightexpressdev.com/excellentshop/chest/claim-integrations) - 限制在玩家领地外创建商店！
  - [**无限存储**](https://nightexpressdev.com/excellentshop/chest/infinite-storage) - 通过虚拟物品存储绕过方块的库存容量限制。
  - **物品黑名单** - 防止具有特定名称、lore 和类型的物品被添加到商店！
- [**拍卖行**](https://nightexpressdev.com/excellentshop/auction/overview) - 允许玩家在全局服务器市场上交易物品！
  - **分类系统** - 使用完全可自定义的物品分类过滤拍卖物品！
  - **排序功能** - 按价格、日期、所有者、类型和名称排序拍卖物品！
  - **通知系统** - 通知玩家已售出、过期和未领取的拍卖！
  - **公告系统** - 在拍卖行添加新拍卖时广播消息！
  - **费用系统** - 向玩家收取在拍卖行添加物品的费用！
  - **容器预览** - 在购买前预览潜影盒和箱子的内容！
  - [**拍卖数量**](https://nightexpressdev.com/excellentshop/auction/listings-amount) - 根据玩家的等级或权限设置可在拍卖行添加的拍卖数量！
  - [**价格限制**](https://nightexpressdev.com/excellentshop/auction/price-limits) - 限制特定物品和货币的拍卖价格！
- **交易日志** - 在专用日志文件中记录所有商店交易！
- [**PlaceholderAPI**](https://nightexpressdev.com/excellentshop/placeholders/papi) 支持。

## 系统要求
- 服务端软件: [**Spigot**](https://www.spigotmc.org/link-forums/88/) 或 [**Paper**](https://papermc.io/downloads/paper)
- 服务端版本: <span style="color:red">**1.21.1**</span> 或更高
- Java 版本: [**21**](https://adoptium.net/temurin/releases) 或更高
- 依赖项:
  - [**nightcore**](https://nightexpressdev.com/nightcore/) - 插件引擎。
- 可选插件:
  - [**PacketEvents**](https://spigotmc.org/resources/80279/) - 箱子商店全息图。
- Folia 支持: <span style="color:red">**否**</span>
- Forge 支持: <span style="color:red">**否**</span>

## 链接
- [原版 SpigotMC](https://spigotmc.org/resources/50696/)
- [原版 BuiltByBit](https://builtbybit.com/resources/46692/)
- [文档](https://nightexpressdev.com/excellentshop/)
- [开发者 API](https://nightexpressdev.com/excellentshop/developer-api/)
- [自定义物品集成文档](CUSTOM_ITEMS_INTEGRATION.md) ⭐ Fork 版本新增

## 致谢

**原作者**: [NightExpress](https://github.com/nulli0n)
- 感谢原作者创建了这个优秀的插件！
- 如果您喜欢原作者的工作，可以 [请他喝杯咖啡](https://ko-fi.com/nightexpress) :) 🧡

**Fork 版本**: 本 Fork 版本添加了完整的自定义物品插件集成支持。