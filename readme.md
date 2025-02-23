<div align="center">

# `jrrpReloaded`

<h1>原先的两个项目已经弃坑!!本项目不会再支持QQ机器人!</h1>
<p>一个基于随机数的衡量玩家幸运程度的插件</p>
<p>
  <img src="https://forthebadge.com/images/badges/made-with-java.svg">
  <img src="https://forthebadge.com/images/badges/built-with-love.svg">

  <br>
  <img src="https://img.shields.io/badge/SPIGOT-1.12+-orange?style=for-the-badge&logo=">
  <img src="https://img.shields.io/badge/JDK-1.8-yellow?style=for-the-badge&logo=appveyor&logo=">
  <a href="https://www.codefactor.io/repository/github/lichris93/jrrpreloaded"><img src="https://www.codefactor.io/repository/github/lichris93/jrrpreloaded/badge" alt="CodeFactor" /></a>
  <br>
  <a href="https://wakatime.com/@09cb58b5-ccc0-41b8-a821-92fbfde0608f"><img src="https://wakatime.com/badge/user/09cb58b5-ccc0-41b8-a821-92fbfde0608f.svg" alt="Total time coded since Jan 13 2023" /></a>
</p>



</div>

---

<div align="left">

## 最新版本

- [x] Pre-release: 无
- [x] Release: 1.0.2

## 服务端支持

- [x] Spigot 1.12+

## 命令列表([]为可选,<>为必填)

- /jrrp - 获取今日人品值
    - help - 获取命令帮助
    - rank - 排行榜
    - getaward - 领取奖励(仅当本功能开启时可用)
    - clear [name] - 清空指定玩家数据/所有数据
    - get \<name\> - 获取指定玩家的人品值
    - set \<name\> \<value\> - 更改指定玩家的人品值
    - reload - 重载config
    - save - 保存数据
    - monitor - 监测各线程运行状态
    - start \<thread\> 启动线程
    - stop \<thread\> 停止线程

## 权限列表
- jrrp.essential - 使用/jrrp,/jrrp getaward,/jrrp rank (请务必把此项权限给所有玩家)
- jrrp.clear - 使用/jrrp clear
- jrrp.get - 使用/jrrp get
- jrrp.set - 使用/jrrp set
- jrrp.reload - 使用/jrrp reload
- jrrp.save - 使用/jrrp save
- jrrp.monitor - 使用/jrrp monitor
- jrrp.start - 使用/jrrp start
- jrrp.stop - 使用/jrrp stop

## 变量列表(需安装PlaceholderAPI)

- %jrrp_num% - 自己的人品值
- %jrrp_num_colored% - 带有颜色的自己的人品值
-
- %jrrp_first_player% - 第一名的名字
- %jrrp_second_player% - 第二名的名字
- %jrrp_third_player% - 第三名的名字
- %jrrp_first_num% - 第一名的人品值
- %jrrp_second_num% - 第二名的人品值
- %jrrp_third_num% - 第三名的人品值
- %jrrp_first_num_colored% - 带有颜色的第一名的人品值
- %jrrp_second_num_colored% - 带有颜色的第二名的人品值
- %jrrp_third_num_colored% - 带有颜色的第三名的人品值
- 
- %jrrp_award_available_status% - 奖励是否可被领取
-
- %jrrp_yesterday_first_player% - 昨天的第一名的名字
- %jrrp_yesterday_second_player% - 昨天的第二名的名字
- %jrrp_yesterday_third_player% - 昨天的第三名的名字
- %jrrp_yesterday_first_num% - 昨天的第一名的人品值
- %jrrp_yesterday_second_num% - 昨天的第二名的人品值
- %jrrp_yesterday_third_num% - 昨天的第三名的人品值
- %jrrp_yesterday_first_num_colored% - 带有颜色的昨天的第一名的人品值
- %jrrp_yesterday_second_num_colored% - 带有颜色的昨天的第二名的人品值
- %jrrp_yesterday_third_num_colored% - 带有颜色的昨天的第三名的人品值

## TODO

- [x] 支持PlaceholderAPI
- [x] 支持榜单
- [x] 按照名次给玩家发放物品奖励(只给第一名发)
-  - [x] 前置要求:记录昨天的数据
- [x] 关闭插件时把数据保存到yml,加载插件时再次读取(有点难)
- [x] 把目前OP可用的指令的权限拆分为权限组
- [x] 把langs和config拆开
- [x] 用指令控制线程的运行
- [ ] 当玩家生成的人品值为100时，触发即时奖励

# 声明

这玩意一开始是写给自己服务器用的，可能不会频繁更新

## 致谢

感谢 PlaceholderAPI

</div>