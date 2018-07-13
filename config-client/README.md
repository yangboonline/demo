## spring cloud config client
> @author yangbo @date 2018/7/13 

### 构建一个config client

1、 maven依赖
```xml
<dependencies>
    <!-- spring cloud config client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <!-- 启用spring mvc -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- 监控和管理生产环境的模块 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Rabbitmq模块 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
2、 在程序的入口Application类加上@RefreshScope注解开启自动刷新功能，代码如下：
```java
@Slf4j
@RefreshScope //启用自动刷新
@RestController
@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @Value("${foo}")
    private String foo;

    @Value("${foo1}")
    private String foo1;

    @RequestMapping(value = "/hi")
    public String hi() {
        log.info("###foo:{}", foo);
        log.info("###foo1:{}", foo1);
        return foo;
    }

}
```
3、 新建application.yml，配置config-client端口等信息：
```yaml
# 项目端口
server:
  port: 8089
```
> SpringCloud里面有个“启动上下文”，主要是用于加载远端的配置，也就是加载ConfigServer里面的配置，默认加载顺序为：加载bootstrap.*里面的配置 --> 链接configserver，加载远程配置 --> 加载application.*里面的配置；

4、新建bootstrap.yml，配置项目不变的配置：
```yaml
spring:
  # 客户端项目名
  application:
    name: config-client
  cloud:
    config:
      username: admin # server basic认证账号
      password: admin # server basic认证密码
      label: master
      profile: dev
      uri: http://localhost:8088/
    # 启用spring消息总线
    bus:
      trace:
        enabled: true # 设置节点状态跟踪，也可以通过网页 http://localhost:8089/trace 可以看到相关发送事件的数据内容
  # rabbitmq配置
  rabbitmq:
    host: localhost   # 登录 Rabbitmq 后台管理页面地址为：http://localhost:15672
    port: 5672
    username: admin   # 默认账户 guest
    password: admin   # 默认密码 guest

# 通过spring-cloud-bus动态刷新配置 http://localhost:8089/actuator/bus-refresh
management:
  endpoint:
    bus-refresh:
      enabled: true
  endpoints:
    web:
      exposure:
        include: bus-refresh
```
5、http请求地址和资源文件映射如下
```text
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```