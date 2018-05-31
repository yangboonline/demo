mybatis-generator官方网址:http://www.mybatis.org/generator/index.html

1.MyBatis Dynamic SQL模式下,生成动态SQl,需要引入
<dependency>
    <groupId>org.mybatis.dynamic-sql</groupId>
    <artifactId>mybatis-dynamic-sql</artifactId>
    <version>1.1.0</version>
</dependency>

2.generatorConfig.xml文件默认路径在resources下
获取generatorConfig.xml办法:https://github.com/mybatis/generator下载源码
可用第三方插件:https://github.com/mybatis/generator/wiki/Third-Party-Tools

3.与Lombok结合需要在generatorConfig.xml引入自己编译的插件
插件地址:https://github.com/WGKLoveStudy/mbg_lombok_plugin
注意:手动下载编译

4.MyBatis3DynamicSql模式下需要引入mybatis-dynamic-sql,且不会生成Mapper.xml;
  MyBatis3模式下会生成对应的Mapper.xml文件
  
参考资料:https://blog.csdn.net/h295928126/article/details/54090796