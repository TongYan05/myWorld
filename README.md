NEXMAR 船舶数智管控中枢
NEXMAR Intelligent Vessel Management Platform
项目简介
Project Overview
独立开发全栈船舶企业数字化管控平台，统一管理船舶、船员、航次、任务分配全业务模块，可承载 620 万 + 船舶业务数据，基于 Docker 容器化部署至云服务器。
Independently developed a full-stack digital management platform for shipping enterprises, integrating full-business modules including vessels, seafarers, voyages and task allocation, supporting over 6.2 million vessel business data records and deployed on cloud servers with Docker containerization.
结合船舶工程专业背景搭建行业定制化管理系统，完成海量数据迁移、SQL 性能调优、多线程批量处理、前后端分离架构落地。
Built an industry-customized management system leveraging marine engineering expertise, realizing massive data migration, SQL performance tuning, multi-thread batch processing and separated front-end & back-end architecture.
完整百万级数据库 SQL 备份文件本地独立存储，未上传至代码仓库。
Complete million-row database SQL backup files are stored locally and never uploaded to code repositories.
技术栈
Tech Stack
后端 DigitalManagement
Backend Module: DigitalManagement
核心框架：SpringBoot、MyBatis-Plus
Core Frameworks: SpringBoot, MyBatis-Plus
数据库：MySQL、Druid 连接池
Database: MySQL, Druid Connection Pool
并发工具：ExecutorService、CountDownLatch
Concurrency Utilities: ExecutorService, CountDownLatch
构建工具：Maven
Build Tool: Maven
部署运维：Docker、Nginx、Linux、AWS / 阿里云 EC2
Deployment & OPS: Docker, Nginx, Linux, AWS & Alibaba Cloud EC2
前端 ship-frontend
Frontend Module: ship-frontend
框架：Vue3 + Vite
Framework: Vue3 + Vite
UI 组件：Element Plus
UI Component Library: Element Plus
网络请求：Axios
HTTP Request Tool: Axios
语法：Composition API
Coding Syntax: Composition API
核心功能与技术亮点
Core Functions & Technical Highlights
海量数据迁移方案
Massive Data Migration Solution
设计分层校验批量导入逻辑，关闭外键约束加速迁移，稳定处理 621 万条船舶数据，零数据丢失，大幅缩短导入耗时。
Designed batch import logic with layered verification, disabled foreign key constraints to speed up migration, stably processed 6.21 million vessel data without data loss and greatly cut import time.
数据库性能调优
Database Performance Optimization
优化索引与执行计划，配合 Druid 连接池调参，接口查询耗时从 2s 优化至 200ms；标准化 Controller-Service-Mapper 三层 RESTful 接口，自定义跨域配置。
Optimized indexes and SQL execution plans, adjusted Druid pool parameters, reducing API query latency from 2s to 200ms; built standardized 3-tier RESTful APIs and customized global cross-domain configuration.
多线程批量数据处理
Multi-thread Batch Data Processing
基于线程池实现大数据分片并行生成，提升压测模拟数据的生成吞吐量。
Realized parallel generation of sharded big data via thread pool to boost throughput of pressure test mock data.
船舶行业领域建模
Shipping Industry Domain Modeling
分层设计船舶 20 + 项技术参数数据结构，前端实现船队总览与单船详情联动编辑，保证数据一致性。
Designed layered data structures covering over 20 vessel technical parameters; front-end supports linked editing between fleet overview and single vessel details to ensure data consistency.
容器化云部署
Containerized Cloud Deployment
Docker 打包前后端全套服务，Nginx 反向代理负载均衡，一套环境适配开发 / 生产，保证运行环境统一。
Packaged full front-end and back-end services with Docker, deployed Nginx reverse proxy and load balancing; one environment fits development & production to unify operating environments.
本地启动步骤
Local Startup Guide
后端启动
Backend Startup
修改 application.yml 中 MySQL 连接地址、账号密码
Modify MySQL address, username and password in application.yml
Maven 刷新依赖，运行 DigitalManagementApplication 启动类
Refresh Maven dependencies and run main class DigitalManagementApplication

### 前端启动
```bash
# 安装依赖
npm install
# 本地调试运行
npm run dev
# 打包线上部署
npm run build

