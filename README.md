# HttpQQrobot - 基于NapCatQQ的智能QQ机器人

## 项目简介

HttpQQrobot是一个基于Spring Boot框架开发的QQ机器人项目，通过与[NapCatQQ](https://github.com/NapNeko/NapCatQQ)正反向代理配合使用，提供智能对话、群消息管理、Steam游戏打折通知等丰富功能。

项目集成了通义千问AI模型，支持上下文记忆的智能对话，并具备完善的权限管理系统和限流机制。

## 功能特性

### 核心功能
- **智能对话**：集成通义千问AI，支持上下文记忆（每个群每个人独立记忆空间）
- **AI联网搜索**：通过Google搜索API实现AI联网查询，解决知识库过时问题
- **群消息总结**：自动总结指定日期的群聊消息内容
- **Steam打折通知**：订阅Steam游戏打折通知，自动推送降价信息
- **权限管理**：多级权限控制（超级管理员、管理员、用户、游客、封禁）
- **消息记录**：自动保存所有消息记录到数据库

### 技术特性
- **限流保护**：基于AOP的接口限流，防止恶意请求
- **动态配置**：通过Nacos实现配置热更新
- **定时任务**：集成XXL-JOB分布式任务调度
- **代理支持**：支持HTTP/HTTPS代理，便于访问外网API

## 技术栈

- **后端框架**：Spring Boot 2.3.5
- **数据库**：MySQL 8.0 + MyBatis-Plus 3.2.0
- **配置中心**：Nacos 2.0.4
- **任务调度**：XXL-JOB 2.4.0
- **工具库**：Hutool 5.8.16、Fastjson 2.0.31
- **其他**：Lombok、JSch、Velocity

## 项目结构

```
HttpQQrobot/
├── src/main/java/com/httpqqrobot/
│   ├── annotation/                         # 自定义注解
│   │   ├── Authorize.java                  # 权限验证注解
│   │   ├── ChainSequence.java              # 处理链顺序注解
│   │   └── RateLimit.java                  # 限流注解
│   ├── aop/                                # 切面类
│   │   ├── AuthorizeAop.java               # 权限验证切面
│   │   └── RateLimitAop.java               # 限流切面
│   ├── chain/                              # 责任链模式处理器
│   │   ├── FunctionHandlerChain.java
│   │   └── functionHandler/
│   │       ├── FunctionHandler.java
│   │       ├── common/CommonMethod.java    # 公共方法
│   │       └── impl/
│   │           ├── AddUserMessage.java     # 消息记录
│   │           └── Dialogue.java           # 对话处理
│   ├── config/                             # 配置类
│   │   ├── NacosConfig.java
│   │   ├── ThreadPoolConfig.java
│   │   └── XxlJobConfig.java
│   ├── constant/                           # 常量类
│   │   └── AppConstant.java
│   ├── controller/                         # 控制器
│   │   └── HttpQQrobotMainController.java
│   ├── entity/                             # 实体类
│   │   ├── AIRequestBody.java
│   │   ├── UserMessage.java
│   │   ├── UserAuthority.java
│   │   ├── SteamDiscountNotify.java
│   │   └── UserRoleEnum.java
│   ├── exception/                          # 异常处理
│   │   ├── AuthorizeException.java
│   │   ├── RateLimitException.java
│   │   └── handler/GlobalExceptionHandler.java
│   ├── job/                                # 定时任务
│   │   └── HttpQQrobotJob.java
│   ├── listener/                           # 监听器
│   ├── mapper/                             # Mapper
│   ├── result/                             # 统一响应结果
│   ├── service/                            # 服务层
│   └── utils/                              # 工具类
│       ├── GoogleUtil.java                 # Google搜索工具
│       ├── RobotUtil.java                  # 机器人工具
│       └── RequestHolderUtil.java          # 请求上下文
├── sql/
│   └── httpqqrobot.sql                     # 数据库脚本
└── pom.xml
```

## 快速开始

### 环境要求

- JDK 8+
- MySQL 8.0+
- Maven 3.6+
- Nacos 2.0+
- XXL-JOB 2.4+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/yourusername/HttpQQrobot.git
   cd HttpQQrobot
   ```

2. **创建数据库**
   ```bash
   mysql -u root -p < sql/httpqqrobot.sql
   ```

3. **修改配置文件**

   编辑 `src/main/resources/application.yml`：
   ```yaml
   robot:
     qq: 你的机器人QQ号
     ip: NapCatQQ服务地址（默认127.0.0.1:3000）
   
   spring:
     datasource:
       url: jdbc:mysql://数据库地址:3306/httpqqrobot
       username: 数据库用户名
       password: 数据库密码
   
   tongyiqianwen:
     apikey: 通义千问API密钥
   
   googleSearch:
     apikey: Google搜索API密钥
     searchEngineID: Google搜索引擎ID
   ```

4. **配置NapCatQQ**

   按照[NapCatQQ文档](https://github.com/NapNeko/NapCatQQ)配置正反向代理，将HTTP上报地址设置为：
   ```
   http://你的服务器地址/napcat
   ```

5. **启动项目**
   ```bash
   mvn spring-boot:run
   ```

## 使用指南

### 基础命令

所有命令都需要通过 `@机器人` 触发：

| 命令 | 说明 | 示例 |
|------|------|------|
| 任意对话 | 与机器人智能对话 | `@机器人 今天天气怎么样` |
| 清除记忆 | 清除上下文记忆 | `@机器人 清除记忆` |
| 群消息总结 | 总结指定日期群消息 | `@机器人 群消息总结 2025-01-20` |
| AI联网 | 启用联网搜索回答 | `@机器人 AI联网 最新的AI技术发展` |
| 菜单 | 显示功能菜单 | `@机器人 菜单` |

### Steam功能

| 命令 | 说明 | 示例 |
|------|------|------|
| Steam打折消息订阅 | 订阅游戏打折通知 | `@机器人 Steam打折消息订阅 https://store.steampowered.com/app/730/` |
| Steam打折消息订阅查询 | 查询已订阅列表 | `@机器人 Steam打折消息订阅查询` |
| Steam打折消息订阅删除 | 删除订阅 | `@机器人 Steam打折消息订阅删除 https://store.steampowered.com/app/730/` |

### 管理命令

需要相应权限才能使用：

| 命令 | 所需权限 | 说明 | 示例 |
|------|----------|------|------|
| admin | 用户以上 | 显示管理菜单 | `@机器人 admin` |
| ban | 管理员 | 封禁用户 | `@机器人 ban 123456789` |
| unban | 管理员 | 解封用户 | `@机器人 unban 123456789` |
| giveadmin | 超级管理员 | 授予管理员权限 | `@机器人 giveadmin 123456789` |
| revokeadmin | 超级管理员 | 撤销管理员权限 | `@机器人 revokeadmin 123456789` |

## 权限系统

项目采用数值权限系统，权限值越高权限越大：

| 角色 | 权限值 | 说明 |
|------|--------|------|
| SuperAdmin | 9 | 超级管理员，拥有所有权限 |
| Admin | 7 | 管理员，可以封禁/解封用户 |
| User | 5 | 普通用户 |
| Guest | 1 | 游客（默认权限） |
| Banned | 0 | 被封禁用户，无法使用机器人 |

## 高级配置

### Nacos配置中心

可以在Nacos中配置以下内容：

1. **排除词配置**（excludeWords-dev）：设置AI回复需要过滤的词汇
2. **提示词配置**（promptWords-dev）：自定义AI的系统提示词
3. **通用配置**（common-dev）：包括AI模型、上下文数量等

### 代理配置

如果需要通过代理访问外网API（如Google搜索、Steam商店），在配置文件中设置：

```yaml
http-https:
  proxy:
    ip: 代理服务器IP
    port: 代理端口
```

### 定时任务

项目集成了XXL-JOB，可以配置Steam打折通知等定时任务。在XXL-JOB管理后台添加任务，JobHandler设置为 `steamDiscountNotify`。

## API接口

### 主要接口

- **POST /napcat** - QQ机器人消息接收接口
- **POST /wechatService** - 微信服务号接口（预留）

### 限流机制

接口使用令牌桶算法限流，默认配置：
- /napcat 接口：每秒最多5次请求

可通过 `@RateLimit` 注解自定义限流参数。

## 开发指南

### 添加新功能

1. 在 `chain/functionHandler/impl/` 目录下创建新的处理器类
2. 实现 `FunctionHandler` 接口
3. 使用 `@ChainSequence` 注解指定处理顺序
4. 在 `Dialogue.java` 中添加命令分发逻辑

### 添加新的定时任务

1. 在 `HttpQQrobotJob.java` 中添加新方法
2. 使用 `@XxlJob` 注解标记
3. 在XXL-JOB管理后台配置任务

## 注意事项

1. 首次使用需要在数据库中手动添加超级管理员权限
2. Steam打折通知功能需要配置代理才能正常访问Steam商店
3. AI联网功能需要配置Google搜索API
4. 建议定期清理 `user_message` 表中的历史消息记录