/**
 * ========================================================================
 *                      个人简历 · 船舶数字化管理系统项目经历
 * ========================================================================
 *
 * 【项目背景】
 *   澳大利亚国立大学（ANU）计算机科学方向，具备扎实的计算机基础与全栈开发能力。
 *   本项目为独立设计、开发并部署上线的船舶数智管控平台，面向航运企业场景，
 *   实现船舶、船员、航次、任务分配及用户的全面数字化管理。
 *
 * ========================================================================
 *                          项目：船舶数字化管理系统（NEXMAR）
 * ========================================================================
 *
 * 【项目简介】
 *   基于前后端分离架构的船舶数智管控平台，涵盖船舶信息、船员管理、航次记录、
 *   任务分配、用户管理五大核心业务模块，系统总业务数据量达 621 万条，
 *   已独立完成阿里云 ECS 线上部署，实现 7×24 小时稳定在线访问。
 *
 * ------------------------------------------------------------------------
 * 一、技术栈（全栈）
 * ------------------------------------------------------------------------
 *
 * ▶ 前端技术栈：
 *   · Vue 3.5 —— 采用 Composition API + <script setup> 组合式语法，
 *     实现响应式数据绑定与组件逻辑复用
 *   · Vite 8 —— 下一代前端构建工具，基于原生 ES Module 的开发服务器，
 *     支持 HMR 热更新，生产构建基于 Rollup
 *   · CSS3 —— CSS Variables 自定义主题变量、Glassmorphism 毛玻璃效果、
 *     Keyframes 动画、Flex/Grid 响应式布局
 *   · Vue Transitions —— 组件级路由切换过渡动画（fade / panel 切换）
 *   · localStorage —— 前端登录会话状态持久化，实现无感刷新保持登录
 *   · Vite Dev Server Proxy —— 开发环境 /api 路径反向代理至后端 2005 端口，
 *     解决前后端分离跨域问题
 *   · Google Fonts（Orbitron + Inter）—— 科技感 UI 字体方案
 *   · 内联 SVG 图标系统 —— 零依赖轻量图标方案，无第三方图标库开销
 *
 * ▶ 后端技术栈：
 *   · Java 17（LTS 长期支持版本）
 *   · Spring Boot 3.5（基于 Spring Framework 6，Jakarta EE 规范）
 *   · Spring Web MVC —— RESTful API 设计，支持 GET/POST/PUT/DELETE 全 CRUD
 *   · MyBatis-Plus 3.5.9 —— 增强版 MyBatis ORM 框架
 *     - MybatisPlusInterceptor + PaginationInnerInterceptor（MySQL 分页拦截器）
 *     - mybatis-plus-extension（分页插件扩展模块）
 *     - mybatis-plus-jsqlparser（JSqlParser SQL 语义解析，自动改写 COUNT 查询）
 *     - 驼峰-下划线自动映射（map-underscore-to-camel-case）
 *   · Alibaba Druid 1.2.28 —— 高性能数据库连接池
 *     （初始连接 5 / 最小空闲 5 / 最大活跃 20 / 最大等待 60s）
 *   · MySQL Connector/J 8.0 —— MySQL 官方 JDBC 驱动
 *   · Lombok 1.18 —— 编译期注解处理器，简化 Entity 样板代码
 *   · DataFaker 2.7 —— 基于中文 Locale 的模拟数据批量生成
 *   · Maven —— 项目依赖管理与构建工具
 *   · CORS 全局跨域配置（WebConfig，支持全域名、全方法、携带凭证）
 *
 * ▶ 数据库：
 *   · MySQL 8.0（InnoDB 引擎，utf8mb4 字符集，支持外键约束与事务）
 *   · 5 张核心业务表：
 *     ship_info（船舶信息，1 万条）
 *     ship_crew_members（船员管理，20 万条）
 *     ship_voyages（航次记录，100 万条）
 *     ship_crew_assignments（任务分配/任职履历，400 万条）
 *     user_info（用户管理，100 万条）
 *   · 表间通过外键关联（ship_id / crew_id），保证数据完整性
 *   · 批量插入优化：rewriteBatchedStatements=true，batchSize=5000
 *
 * ▶ 部署与运维：
 *   · 阿里云 Ubuntu 轻量应用服务器 ECS
 *   · Linux Shell 命令运维（nohup / ps / systemctl / mysql 命令行）
 *   · SQLyog —— 单表导出含 DROP TABLE 语句的 SQL 脚本
 *   · 阿里云 Workbench 网页终端 —— 远程服务器管理
 *
 * ------------------------------------------------------------------------
 * 二、核心职责与成果
 * ------------------------------------------------------------------------
 *
 * 1.【全栈独立开发】
 *   独立完成系统前后端架构设计、数据库建模（5 表外键关联）、
 *   RESTful API 接口开发（5 组 Controller / Service / Mapper 分层架构）
 *   及前端 5 大业务管理面板的组件化开发，
 *   后端统一 /api 上下文路径，前端通过 Axios 异步调用，
 *   CORS 全局跨域配置实现前后端分离部署。
 *
 * 2.【621 万级全量数据迁移与一致性校验】
 *   独立完成本地到线上 MySQL 的全量业务数据迁移（共 621 万条）：
 *   · 通过 SQLyog 逐表导出含 DROP TABLE 语句的 SQL 脚本，
 *     确保导入时先删后建，避免数据残留与主键冲突
 *   · 使用 Linux 终端 MySQL 命令行批量导入 SQL 脚本，
 *     完成 400 万级超大表（ship_crew_assignments）的数据覆盖同步
 *   · 采用单表分步导入策略，规避全库一次性导入导致的数据残缺、
 *     主键重复及外键约束冲突问题
 *   · 完成线上数据库数据校验，通过 COUNT 比对与抽样核查，
 *     保障本地与服务器数据库数据完全一致
 *
 * 3.【后端服务常驻部署与进程监控】
 *   · 使用 nohup java -jar 命令实现 Spring Boot Jar 包后台常驻运行，
 *     日志输出重定向至 nohup.out，解决 SSH 终端关闭后服务自动中断问题
 *   · 搭配 ps -ef | grep java 进程监控指令实时检查服务运行状态，
 *     发现进程异常退出时及时重启，保障系统 7×24 小时不间断在线
 *
 * 4.【数据库连接池调优】
 *   配置 Alibaba Druid 连接池核心参数：
 *   initial-size=5 / min-idle=5 / max-active=20 / max-wait=60000ms，
 *   保障高并发场景下数据库连接的高效复用与请求排队控制，
 *   避免连接泄漏与数据库过载。
 *
 * 5.【分页查询性能优化】
 *   基于 MyBatis-Plus PaginationInnerInterceptor 实现服务端分页，
 *   JSqlParser 自动将业务 SQL 改写为 SELECT COUNT(*) 统计查询，
 *   减少全表扫描开销；接口层设置单页上限 100 条的限流策略
 *   （if count > 100 then count = 100），防止大页查询导致内存溢出。
 *
 * 6.【前端工程化与组件化实践】
 *   采用 Vue 3 Composition API 组合式语法，按业务模块拆分为
 *   ShipInfoPanel / CrewPanel / VoyagePanel / AssignmentPanel / UserPanel
 *   五大独立组件，由 DashboardView 统一调度切换；
 *   Vite 构建打包后生成 dist 静态资源，部署至服务器，
 *   实现前后端独立部署、独立更新、互不影响。
 *
 * 7.【大规模模拟数据生成】
 *   基于 DataFaker 2.7（中文 Locale）编写批量数据生成测试，
 *   为 5 张业务表生成符合真实业务逻辑的模拟数据
 *   （船员姓名、船舶编号、航次港口、岗位薪资等），
 *   单表最高 400 万条，验证系统在大数据量下的查询与分页性能。
 *
 * ------------------------------------------------------------------------
 * 三、解决的问题
 * ------------------------------------------------------------------------
 *
 * · 全库导入数据残缺 → 改为单表分步导出导入，带 DROP TABLE 先删后建
 * · 主键重复冲突 → 导出脚本含自增 ID 重置，导入前清空目标表
 * · 外键约束导致导入失败 → 导入前临时关闭 FOREIGN_KEY_CHECKS
 * · SSH 终端关闭服务中断 → nohup 后台常驻 + ps 进程监控
 * · 前后端跨域 → 后端 WebConfig CORS 全局配置 + 前端 Vite Proxy 开发代理
 * · 大页查询 OOM 风险 → 服务端分页 + 单页 100 条限流
 * · 驼峰字段映射不一致 → MyBatis-Plus map-underscore-to-camel-case 自动映射
 * · 批量插入性能低 → rewriteBatchedStatements=true 开启批量重写
 *
 * ========================================================================
 */

