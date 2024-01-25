## one-logger

仅需一个日志框架，打印全流程审计日志

### 一、目标

    在零配置的情况下，无感知的支持主流各个中间件打印，支持链路，后续将支持对接第三方监控平台。
    强耦合Springboot环境，是使用aop对中间件底层方法进行代理，打印日志。

### 二、Features
1.	异步打印，不影响主流程耗时	AsyncAuditPool	开启线程池，对外方法，统一打印

2.	日志分层	统一logger-name，logback.xml 设置不同的日志打印方式

3.	支持controller http请求	httpserlvet和切面结合

4.	支持resttemplate中间件日志打印

5.	支持rabbitmq中间件日志打印

6.	支持kafka中间件日志打印

7.	支持mysql日志打印	【支持mybatis框架或支持druid数据源方式】

8.	支持Redis日志打印	redisTemplate自定义切面 

9.  支持spi扩展，支持日志字段脱敏、过滤，格式自定义

---
### 三、日志格式

默认支持两种格式 ```json``` 和 ```plain``` ，也支持自定义格式，通过spi实现

字段

| 字段              | 含义                              | 示例                                                                 |
|-----------------|---------------------------------|--------------------------------------------------------------------|
| timestamp       | 打印时的时间戳 yyyy-MM-dd HH:mm:ss.SSS | 2024-01-23 18:45:56.035                                            |
| traceId         | 请求id, uuid                      | d2c0ec12c8fb489299b1e587e36ce683                                   |
| applicationName | 应用名称                            | demo-service                                                       |
| logType         | 日志类型，区分什么类型                     | WEB/DB/RABBITMQ.SEND                                               |
| action          | 该日志的具体行为                        | WEB./demo/findList                                                 |
| rootSpan        | 该日志的根spanId，用于链路追踪              | WEB./demo/findList                                                 |
| interface       | 该日志的具体执行入口，用户代码分析               | com.demo.controller.ActivityBriefController                        |
| method          | 该日志的具体执行方法，用户代码分析               | findList                                                           |
| connId          | 链接地址                            | 0:0:0:0:0:0:0:1                                                    |
| sourceIp        | 源ip地址                           | 127.0.0.1                                                          |
| spanId          | 调用链，spanId                      | 0.1.1.2                                                            |
| sequence        | 被调用次数                           | 10                                                                 |
| cost            | 耗时（ms)                          | 10                                                                 |
| request         | 请求参数，json格式                     | "request":{"qp":{"pageNum":1,"pageSize":10,"id":"11","name":"123"} |
| response        | 响应参数，json格式                     | "response":{"retrun_code":0,"data":[]}                             |
| extraInfo       | 额外参数，http header、mysql地址等       | "extraInfo":"clientIp=0:0:0:0:0:0:0:1;clientPort=60833;header={};" |


- plain
  字符切分为 三个空格 + ,    
  ```text
    2024-01-25 10:29:57.049,   6722df6f783b4c58914cedbbc0f4d3cc,   dem-service,   WEB,   WEB./demo/findList,   WEB./demo/findList,   com.demo.controller.DemoController,   findList,   0:0:0:0:0:0:0:1,   127.0.0.1,   0.1,   1,   50516,   requestBody:{"pageNum":0,"pageSize":0,"id":0,"name":"123"},   retrun_code:0^_^data:[],   clientIp=0:0:0:0:0:0:0:1;clientPort=65179;header={};
  ```

- json
  ```json
    {
    "timestamp":"2024-01-23 18:45:56.035",
    "requestId":"d2c0ec12c8fb489299b1e587e36ce683",
    "applicationName":"demo-service",
    "logType":"WEB",
    "action":"WEB./demo/findList",
    "rootSpan":"WEB./demo/findList",
    "interface":"com.demo.controller.DemoController",
    "method":"findList",
    "connId":"0:0:0:0:0:0:0:1",
    "sourceIp":"127.0.0.1",
    "spanId":"0.1",
    "sequence":1,
    "cost":107,
    "request":{
        "requestBody":{
            "pageNum":1,
            "pageSize":10,
            "id":"11",
            "name":"123"
        }
    },
    "response":{
        "retrun_code":0,
        "data":[
            {
            }
        ]
    },
    "extraInfo":"clientIp=0:0:0:0:0:0:0:1;clientPort=60833;header={};"
  }
  ```
### 四、How To Use it
4.1 引入依赖
```xml
    <dependency>
      <groupId>fun.pxyc</groupId>
      <artifactId>one-logger</artifactId>
      <version>0.0.1</version>
    </dependency>
```

引入依赖后默认就已经拥有Redis、mq、web、restTemplate等监控能力

4.2 删除之前的aop打印日志(如有，不删也不影响)

4.3 application.yaml配置
```yaml
spring:
  application:
    name: demo-service
log:
  path: ./logs
env:
  config:
    web-cut-point: execution(* com.demo.controller..*(..))
    biz-cut-point: execution(* com.demo.service..*(..))
    log-formatter: plain
```

4.4 logBack.xml配置
增加appender和logger：

 ```xml
 <appender name="SERVERLOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/auditlog/server.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/server.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <appender name="CLIENTLOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/auditlog/client.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/client.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--level为 ERROR 日志，时间滚动输出-->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--正在记录的日志文档的路径及文档名-->
        <file>${logPath}/${appName}/auditlog/error.log</file>
        <!--日志文档输出格式-->
        <encoder>
            <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{TRACE_ID}] [%thread] %-5level %logger{50} - %msg%n</Pattern>
            <!--设置字符串-->
            <charset>UTF-8</charset>
        </encoder>
        <!--日志记录器的滚动策略，按日期,按大小记录-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/error.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <!--此日志文档只记录info级别的-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <logger name="auditlog.server" level="info" additivity="false">
        <appender-ref ref="SERVERLOG" />
    </logger>
    <logger name="auditlog.client" level="info" additivity="false">
        <appender-ref ref="CLIENTLOG" />
    </logger>
<root level="info">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="ROOT"/>
        <appender-ref ref="ERROR"/>
    </root>
 ```
完整的logback.xml

```xml

<?xml version="1.0" encoding="UTF-8"?>

<configuration>

    <property name="logPath" value="D:/opt/logs/"/>
    <property name="appName" value="demo-service"/>
    <property name="FILE_LOG_PATTERN"
              value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
    <property name="COMMON_FORMAT" value="[%d{yyyy-MM-dd'T'HH:mm:ss.sss'Z'}] [%C] [%t] [%L] [%-5p] %m%n"/>

    <!--控制台输出-->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--文件追加-->
    <appender name="ROOT" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/log/root.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/log/root.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>


    <appender name="SYNCALL" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/log/all.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/log/all.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <appender name="ALL" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="SYNCALL"/>
    </appender>


    <appender name="SERVERLOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/auditlog/server.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/server.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <appender name="CLIENTLOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${logPath}/${appName}/auditlog/client.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/client.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--level为 ERROR 日志，时间滚动输出-->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--正在记录的日志文档的路径及文档名-->
        <file>${logPath}/${appName}/auditlog/error.log</file>
        <!--日志文档输出格式-->
        <encoder>
            <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{TRACE_ID}] [%thread] %-5level %logger{50} - %msg%n</Pattern>
            <!--设置字符串-->
            <charset>UTF-8</charset>
        </encoder>
        <!--日志记录器的滚动策略，按日期,按大小记录-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logPath}/${appName}/auditlog/error.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>5</maxHistory>
        </rollingPolicy>
        <!--此日志文档只记录info级别的-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <logger name="auditlog.server" level="info" additivity="false">
        <appender-ref ref="SERVERLOG" />
    </logger>
    <logger name="auditlog.client" level="info" additivity="false">
        <appender-ref ref="CLIENTLOG" />
    </logger>
    <root level="info">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="ROOT"/>
        <appender-ref ref="ERROR"/>
    </root>

</configuration>


```

---
### 五、配置项
| 配置参数                       | 配置枚举                                       | 含义                                                         |
| ------------------------------ | ---------------------------------------------- | ------------------------------------------------------------ |
| env.config.disable-stdout      | false;true                                     | 控制是否输出log.info日志和console日志，默认false;配置为true，则不会打印console日志和info级别日志，慎重配置 |
| env.config.trace-adapter-param | default;....                                   | traceAadpter的spi支持，默认default，后续可以利用此spi对接第三方中间件 |
| env.config.web-cut-point       | 默认:execution(* com.demo.*.controller..*(..)) | web层切点配置                                                |
| env.config.biz-cut-point       | 默认:execution(* com.demo.*.service..*(..))    | biz层切点配置                                                |
| env.config.log-formatter       | json;plain;......                              | logFormatter的spi支持，默认json,支持自定义输出logStr         |
| env.config.log-index           | 例：req.name，可以参考jsonPath，多个用分号拼接 | 提取日志中的某一个字段作为index，方便支持es的索引。原理为：[jsonPath](https://github.com/json-path/JsonPath) |
| env.config.ignore-params       | 例：res.name，可以参考jsonPath，多个用分号拼接 | 忽略日志中的某一个字段，会将该字段值替换为"******"。原理为：[jsonPath](https://github.com/json-path/JsonPath) |
| env.config.slow-sql-millis     | 慢sql时间ms int值                              | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-redis-millis   | 慢redisl时间ms int值                           | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-http-millis    | 慢http时间ms int值                             | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-kafka-millis   | 慢kafka时间ms int值                            | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-mq-millis      | 慢mq时间ms int值                               | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-hbase-millis   | 慢hbase时间ms int值                            | 超过就会在all.log里打印warn级别的日志                        |
| env.config.slow-mongo-millis   | 慢mongo时间ms int值                            | 超过就会在all.log里打印warn级别的日志                        |
| env.config.db-log-max-chars    | 默认：-1（全部打印）db日志输出最大字符         |                                                              |
| env.config.dubbo-log-max-chars | 默认：-1（全部打印）dubbo日志输出最大字符      |                                                              |
| env.config.hbase-log-max-chars | 默认：-1（全部打印）hbase日志输出最大字符      |                                                              |
| env.config.http-log-max-chars  | 默认：-1（全部打印）http日志输出最大字符       |                                                              |
| env.config.mongo-log-max-chars | 默认：-1（全部打印）mongo日志输出最大字符      |                                                              |
| env.config.mq-log-max-chars    | 默认：-1（全部打印）mq日志输出最大字符         |                                                              |
| env.config.redis-log-max-chars | 默认：-1（全部打印）redis日志输出最大字符      |                                                              |
| env.config.web-log-max-chars   | 默认：-1（全部打印）web日志输出最大字符        |                                                              |
| env.config.sample-rate         | 采集率 保留字段 暂时没有用                     |                                                              |

### 六、SPI支持

- LoggerFormatterHandler.class
   <p> 构造输出日志的String格式，框架默认提供：plain和json两种格式，默认为：json输出</p>
- LoggerFilter.class
  <p>类似javaEE的filter，支持在打印日志前后对参数进行处理；框架默认提供：
  loginIndex和ignoreParams，分别能力为：配置自定义logIndex;忽略输出的字符；</p>

### 七、性能测试
[性能测试](/doc/benchmark.md)





