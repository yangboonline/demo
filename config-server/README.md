## spring cloud config server
> @author yangbo @date 2018/7/13 

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

### 构建config-server

1、 maven依赖
```xml
<dependencies>
    <!-- config-server依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    <!-- 为config-server启用安全认证(可选) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
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
2、 在程序的入口Application类加上@EnableConfigServer注解开启配置服务器的功能，代码如下：
```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
```
3、 新建application.yml，配置config-server端口等信息：
```yaml
# 项目端口
server:
  port: 8088

spring:
  # 项目名
  application:
    name: config-server
  cloud:
    config:
      label: master # 分支名
      server:
        # Git托管所有配置
        git:
          uri: https://github.com/yangboonline/config-respo.git
          # 私有仓库需要指定用户名和密码
          username: ******
          password: ******
          # 搜索application名称开头的服务
          search-paths: '{application}'
        # 本地托管所有配置
        native:
          search-locations: /Java/iwork/config
  # 启用basic认证(可选)
  security:
    user:
      name: admin
      password: admin
  # 默认读取本地配置
  profiles:
    active: native # native指定本地, 生成环境务必在启动命令中指定 -Dspring.profiles.active=prod
```
> SpringCloud里面有个“启动上下文”，主要是用于加载远端的配置，也就是加载ConfigServer里面的配置，默认加载顺序为：加载bootstrap.*里面的配置 --> 链接configserver，加载远程配置 --> 加载application.*里面的配置；

4、新建bootstrap.yml，配置项目不变的配置：
```yaml
# 对称加密
# encrypt:
#   key: 6YtbURwm3ELpa1mCj6oVyg==

# 非对称加密
encrypt:
  keyStore:
      location: classpath:server-rsa.jks # key store file 生成方法:cmd执行keytool -genkeypair -alias 别名 -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass 公钥 -keystore server-rsa.jks -storepass 私钥
      password: paic1234 # 公钥
      alias: mytestkey   # 别名
      secret: aaaaa888   # 私钥

# 暴露所有端点(作用是可访问所有spring-cloud内置api)
management:
  endpoints:
    web:
      exposure:
        include: '*'
```
5、http请求地址和资源文件映射如下
```text
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```