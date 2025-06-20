# OnlineShoppingWeb

一个使用 **Java、JSP、Servlet、JDBC** 构建的在线购物系统，包含用户端购物车功能以及管理员后台管理（商品、库存、订单）。

## 🔍 项目概述

- 用户可以浏览商品、添加到购物车、修改数量、下单。
- 管理员可以登录后台管理系统，进行商品分类、商品上架/下架、库存调整、订单查看等操作。
- 适合学习 Java Web 开发全流程，包括前后端交互、会话管理、数据库操作等。

## 🛠 技术栈

| 前端                   | 后端                | 数据库         | 日志  |
| ---------------------- | ------------------- | -------------- | ----- |
| JSP + JSTL + Bootstrap | Java Servlet + JDBC | MySQL / MS SQL | Log4j |



> ※ 可以根据实际改为 MSSQL、PostgreSQL 等，只需修改驱动与连接配置。

## 📂 项目结构（示例）

```
bash复制编辑OnlineShoppingWeb/
├─ src/                # Java 源码（Servlet、DAO、Model 等）
├─ web/                # JSP 页面 + 前端资源（CSS、JS、图片）
├─ lib/                # 第三方依赖 JAR 包（JSTL、数据库驱动等）
├─ database.sql        # 初始化数据库脚本（建表 + 示例数据）
├─ build.xml / pom.xml # 构建配置（Ant 或 Maven）
└─ README.md           # 项目说明
```

## 🚀 快速运行

以下为常规步骤，可根据你的实际开发环境做调整：

1. 克隆代码到本地：

   ```
   bash
   
   
   复制编辑
   git clone https://github.com/Vegcock/OnlineShoppingWeb.git
   ```

2. 导入项目到 IDE（推荐 IntelliJ IDEA 或 Eclipse）：

   - 若使用 NetBeans，也支持直接打开。
   - 确认已添加所有 `lib/` 文件夹中的 JAR 包。

3. 配置数据库：

   - 使用 `database.sql` 初始化数据库结构和样例数据。
   - 修改项目中负责数据库连接的类（如 `DBHelper.java` 或类似类），填写你的 JDBC URL、用户名、密码。

4. 部署运行：

   - 在 IDE 中部署到 Tomcat。

   - 或用 `war` 包手动部署：

     ```
     bash复制编辑mvn clean package
     cp target/OnlineShoppingWeb.war $TOMCAT_HOME/webapps/
     ```

   - 访问 `http://localhost:8080/OnlineShoppingWeb/`

5. 登录后台：

   - 默认管理员账号请参考 `database.sql` 中提供的测试数据（如有）。
   - 没有的情况下可自行添加一条 admin 用户数据。

## 🧪 核心功能

### 用户端

- 浏览商品列表，查看详情。
- 增加/移除购物车中的商品。
- 调整购物车中商品数量。
- 下单并查看订单状态。

### 管理后台

- 用户登录登出（会话管理）。
- 商品分类管理（新增/删除/修改）。
- 商品管理（信息维护、上下架、库存同步）。
- 订单管理（查看所有订单、订单详情）。

## 🔧 进一步改进建议

- 增加用户注册／修改密码功能；
- 增强购物流程：支付接口（如 PayPal、Stripe）集成；
- 增加订单状态更新（已付款、已发货、已完成）；
- 使用 Servlet Filter 做安全与权限验证；
- 使用 Maven 或 Gradle 管理依赖和构建；
- 引入 Spring/Spring MVC 或 Spring Boot 重构，提升代码结构。

## 📝 LICENSE

MIT 协议，欢迎用于学习与改进。
 © 2025 Vegcock