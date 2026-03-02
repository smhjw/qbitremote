# QB Remote (Android MVP)

一个类似你截图风格的 qBittorrent 安卓远程管理端 MVP：

- 连接 qBittorrent Web API
- 登录鉴权（`/api/v2/auth/login`）
- 拉取全局传输信息（`/api/v2/transfer/info`）
- 拉取种子列表（`/api/v2/torrents/info`）
- 暗色任务卡片列表（进度、速率、做种/下载状态）
- 自动轮询刷新
- 任务操作：暂停 / 继续 / 删除（可选删除文件）
- 状态筛选：All / Downloading / Seeding / Paused / Completed / Error

## 1. qBittorrent 端准备

在 qBittorrent（桌面端）里开启 WebUI：

1. `工具 -> 选项 -> Web UI`
2. 勾选 `Web 用户界面（远程控制）`
3. 设置端口（默认 `8080`）
4. 设置用户名/密码
5. 如果你要手机跨网段访问，确认防火墙和路由放行端口

## 2. 安卓端运行

1. 用 Android Studio 打开 `qb-remote-android`
2. 首次打开执行 Gradle Sync
3. 运行到真机或模拟器
4. 在 App 中输入：
   - Host：qB 主机 IP（例如 `192.168.1.12`）
   - 端口：qB WebUI 端口（例如 `8080`）
   - 用户名/密码：qB WebUI 账户
   - 是否 HTTPS：按你的 qB 配置选择
5. 点击“连接”

## 3. 当前限制（MVP）

- 密码当前存储在 DataStore（未做系统级加密）
- HTTPS 自签证书未做信任适配
- 尚未实现加标签、改分类、批量操作

## 4. 下一步建议

- 接入 `EncryptedSharedPreferences` 或 Android Keystore 存储凭据
- 增加任务控制接口：
  - `/api/v2/torrents/pause`
  - `/api/v2/torrents/resume`
  - `/api/v2/torrents/delete`
- 加入搜索、筛选、分组、状态统计面板
- 增加“多服务器配置”
