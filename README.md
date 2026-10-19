# Z80Z Team Deathmatch（团队死斗）

A multiplayer FPS-style team deathmatch mod for Minecraft 1.20.1 Forge — lobbies, matchmaking, three game modes, a map editor, a HUD editor, an economy system, a match shop and a weapon repository.

Minecraft 1.20.1 · Forge 47.4.0 · Java 17 · Client and server

## 功能

### 三种玩法

- **团队竞技**：可复活，按击杀目标或回合时长结算。
- **爆破模式**：回合歼灭制，进攻方携带 C4、防守方拆弹；购买阶段、安装、引爆和拆除时间均可独立配置，支持换边与拆弹进度保留。
- **歼灭竞技**：单局歼灭，全程不复活，最后存活的队伍获胜。

### 大厅与匹配

- 创建房间、6 位邀请码加入、快速匹配、分列队伍与多队对抗。
- 房主可配置比赛模式、回合数、时间、复活规则、队伍人数、地图池等房间规则。
- 大厅支持好友、邀请、房间列表和我的房间管理。

### 地图工作台与编辑器

- 战术地图平板（规划器）与测绘激光笔（画笔），支持区域选择、绘制、逐格编辑。
- 可设置出生点、观战点、随机复活区域和 BOMB 爆破区。
- 区域支持优先级、显示外观、模式条件和条件表达式。

### HUD 编辑器

- 画布内拖拽、滚轮缩放、方向键微调、锚点、对齐辅助线和组件级/整体重置。
- 三套内置模板：CS2 风格、穿越火线风格、使命召唤 20 / MWIII 风格。
- 支持 `{round}`、`{target}`、`{score_a}`、`{ping}` 等实时参数，删除与模板套用均可撤销/重做。

### 经济与商店

- 双钱包：永久保存的大厅账户 + 每场比赛重置的比赛资金。
- 击杀、伤害、回合胜负、连败、比赛结算和 GD656 Score 增量均可通过公式换算。
- 对局商店出售武器、弹药、配件和维修服务，商品来自服务器武器仓库。
- 武器仓库保存完整 ItemStack 快照（含 NBT、配件、皮肤），支持搜索、筛选、添加和领取。

### 兼容

- **GD656**：通过反射读取 Score 增量，未安装时使用本模组自己的结算。
- **GunDB / TACZ**：通过反射读取创造分类、枪械与配件耐久，不添加编译期依赖。
- 以上模组均为可选，不安装也能运行基础玩法。

## 安装

1. 安装 [Minecraft Forge 1.20.1-47.4.0](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)。
2. 从 [Releases](https://github.com/Z80Z99/z80z-team-deathmatch/releases) 下载最新 jar，放入客户端和服务端的 `mods` 文件夹。
3. 首次启动后会生成 `config/fpsmod/` 配置目录，服务器和客户端都需要安装本模组。

## 按键

| 按键 | 功能 |
| --- | --- |
| `K` | 打开地图工作台 |
| `J` | 打开大厅 / 房间菜单 |
| `H` | 打开 HUD 编辑窗口 |
| `B` | 打开对局商店 |

以上按键均可在「选项 → 控制 → 按键绑定」中修改。

## 命令

玩家命令：

- `/fps join [team]`、`/fps leave`、`/fps status`
- `/fps map list`、`/fps map current`
- `/fps money balance`、`/fps shop list`

管理员命令（需要 OP 权限）：

- `/fps start`、`/fps stop`、`/fps restart`
- `/fps map load <地图>`、`/fps map reload`、`/fps setspawn <类型>`
- `/fps money add|set <玩家> <数量>`、`/fps economy reload`
- `/fps shop addheld`、`/fps shop price <条目> <价格>`
- `/fps hudstat list|set|name|remove|clear`

## 从源码构建

需要 JDK 17：

```bash
./gradlew build
```

产物位于 `build/libs/z80z-team-deathmatch-<版本>.jar`。

## 文档

- [经典爆破模式](docs/CLASSIC_BOMB.md)
- [金钱系统](docs/ECONOMY_SYSTEM.md)
- [对局商店](docs/MATCH_SHOP.md)
- [武器仓库](docs/WEAPON_REPOSITORY.md)
- [HUD 编辑器](HUD_EDITOR.md)
- [地图工具](MAP_TOOLS.md)

## 许可证

MIT，详见 [LICENSE](LICENSE)。

