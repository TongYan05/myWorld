# NEXMAR 船舶数智管控中枢
## 项目简介
独立开发全栈船舶企业数字化管控平台，统一管理船舶、船员、航次、任务分配全业务模块，可承载620万+船舶业务数据，基于Docker容器化部署至云服务器。
结合船舶工程专业背景搭建行业定制化管理系统，完成海量数据迁移、SQL性能调优、多线程批量处理、前后端分离架构落地。
完整百万级数据库SQL备份文件本地独立存储，未上传至代码仓库。

## 技术栈
### 后端 DigitalManagement
- 核心框架：SpringBoot、MyBatis-Plus
- 数据库：MySQL、Druid连接池
- 并发工具：ExecutorService、CountDownLatch
- 构建工具：Maven
- 部署运维：Docker、Nginx、Linux、AWS/阿里云EC2

### 前端 ship-frontend
- 框架：Vue3 + Vite
- UI组件：Element Plus
- 网络请求：Axios
- 语法：Composition API

## 核心功能与技术亮点
1. **海量数据迁移方案**
设计分层校验批量导入逻辑，关闭外键约束加速迁移，稳定处理621万条船舶数据，零数据丢失，大幅缩短导入耗时。
2. **数据库性能调优**
优化索引与执行计划，配合Druid连接池调参，接口查询耗时从2s优化至200ms；标准化Controller-Service-Mapper三层RESTful接口，自定义跨域配置。
3. **多线程批量数据处理**
基于线程池实现大数据分片并行生成，提升压测模拟数据的生成吞吐量。
4. **船舶行业领域建模**
分层设计船舶20+项技术参数数据结构，前端实现船队总览与单船详情联动编辑，保证数据一致性。
5. **容器化云部署**
Docker打包前后端全套服务，Nginx反向代理负载均衡，一套环境适配开发/生产，保证运行环境统一。

## 本地启动步骤
### 后端启动
1. 修改 `application.yml` 中MySQL连接地址、账号密码
2. Maven刷新依赖，运行 `DigitalManagementApplication` 启动类

### 前端启动
```bash
# 安装依赖
npm install
# 本地调试运行
npm run dev
# 打包线上部署
npm run build

English Version
NEXMAR Intelligent Vessel Management Platform
Project Overview
Independently developed a full-stack digital management platform for shipping enterprises, integrating full-business modules including vessel management, seaman administration, voyage tracking and task allocation. The system supports over 6.2 million pieces of vessel business data and is deployed on cloud servers via Docker containerization.
Combining professional marine engineering knowledge, I built an industry-specific customized management system, implementing massive data migration, SQL performance optimization, multi-threaded batch processing and a standard separated front-end and back-end architecture.
Complete SQL backup files of million-level database are stored locally and never pushed to code repositories.
Tech Stack
Backend Module: DigitalManagement
Core Frameworks: SpringBoot, MyBatis-Plus
Database: MySQL with Druid Connection Pool
Concurrency Utilities: ExecutorService, CountDownLatch
Build Tool: Maven
DevOps & Deployment: Docker, Nginx, Linux, AWS & Alibaba Cloud EC2
Frontend Module: ship-frontend
Framework: Vue3 + Vite
UI Component Library: Element Plus
HTTP Request Library: Axios
Coding Standard: Composition API
Core Functions & Technical Highlights
Massive Data Migration Solution
Designed a batch import logic with layered verification. Foreign key constraints are temporarily disabled to accelerate migration. The system stably processes 6.21 million vessel data records without any data loss and greatly reduces data import time consumption.
Database Performance Optimization
Optimized database indexes and SQL execution plans, and adjusted parameters of Druid connection pool. The average interface query latency was reduced from 2 seconds to 200 milliseconds. Developed standardized RESTful APIs based on Controller-Service-Mapper three-tier architecture and customized global cross-domain configuration.
Multi-threaded Batch Data Processing
Implemented parallel generation of sharded large datasets via thread pool, which significantly improves throughput for pressure test mock data generation.
Domain-Driven Modeling for Shipping Industry
Constructed layered data structures covering more than 20 technical parameters of vessels. The front-end supports linked editing between fleet overview and single-vessel detail pages to guarantee data consistency.
Containerized Cloud Deployment
Packaged full front-end and back-end services with Docker, configured Nginx reverse proxy and load balancing. One unified image is compatible with both development and production environments to standardize operating environments.
Local Startup Guide
Backend Startup Steps
Modify MySQL connection address, username and password in application.yml
Refresh Maven dependencies and run the main class DigitalManagementApplication to launch backend service
Frontend Startup Commands
bash
运行
# Install dependencies
npm install
# Local development run
npm run dev
# Build project for production deployment
npm run build
