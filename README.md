# 船舶管理后台管理系统
## 项目简介
本项目是一套完整的船舶信息管理全栈系统，包含后端Java服务 + Vue前端页面，实现船员、船舶、航次、用户等数据管理功能，支持Docker一键部署。

## 技术栈
### 后端 DigitalManagement
- 核心框架：SpringBoot
- 数据库：MySQL
- ORM：MyBatis
- 工具：Maven、Docker

### 前端 ship-frontend
- 框架：Vue3
- 构建工具：Vite
- UI组件：Element Plus
- 网络请求：Axios

## 项目模块说明
1. DigitalManagement：后端接口服务，包含用户、船舶、船员、航次业务逻辑
2. ship-frontend：可视化管理前端页面，数据看板、信息增删改查面板
3. docker-compose.yml：容器一键部署配置文件

## 本地启动步骤
### 后端启动
1. 修改application.yml内MySQL数据库连接地址
2. Maven加载依赖，运行DigitalManagementApplication启动类

### 前端启动
```bash
# 安装依赖
npm install
# 本地运行
npm run dev
# 打包部署
npm run build
