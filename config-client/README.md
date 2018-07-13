### 配置服务的路劲规则:

* /{application}/{profile}[/{label}]
* /{application}-{profile}.yml
* /{label}/{application}-{profile}.yml
* /{application}-{profile}.properties
* /{label}/{application}-{profile}.properties

> SpringCloud里面有个“启动上下文”，主要是用于加载远端的配置，也就是加载ConfigServer里面的配置，默认加载顺序为：加载bootstrap.*里面的配置 --> 链接configserver，加载远程配置 --> 加载application.*里面的配置；

> 在ConfigServer服务启动的时候，bootstrap 拿到远端配置注入到 profile 的属性中的话，那么就不会再次覆盖这个属性了，所以只会选择远端配置的内容.
      那是不是会有人认为把ConfigServer再重启一下就行了呢？答案是不行的，因为首选的是远端配置内容.
