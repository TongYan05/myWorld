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
