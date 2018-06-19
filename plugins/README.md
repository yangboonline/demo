1.jaxb2-maven-plugin

介绍:该插件使用XML绑定（JAXB）版本2+的Java API从XML模式（以及可选的绑定文件）生成Java类，
     并从注释的Java类创建XML模式。该插件通过其JAXB实现依赖关系将其大部分工作委托给JDK提供的
   	 两个工具XJC和Schemagen中的任意一个

官网:http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/index.html

github:https://github.com/mojohaus/jaxb2-maven-plugin

问题:locale指定zh、zh_CN、zh_TW时不能正确生成对应语言的源注释

临时解决方法:手动下载源码,修改问题方法.具体操作:https://github.com/mojohaus/jaxb2-maven-plugin/issues/112
