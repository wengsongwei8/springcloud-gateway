##项目说明
* 前后端分离的企业级微服务架构
* 项目整合了SpringBoot、Mybatis、Mybatis-lus、Swagger-ui、Druid、lombok、validator等常用
  框架提供后端开发脚手架。
* 基于`Spring Boot 2.0.X`、`Spring Cloud Finchley`和`Spring Cloud Alibaba`
* 注重代码规范，严格控制包依赖，每个工程基本都是最小依赖
* 引入组件化的思想实现高内聚低耦合，项目代码简洁注释丰富上手容易

       
 ##项目开发环境启动
 
 * 修改本地maven配置文件settings.xml。常见配置如下：
    ```xml
    		<mirror>
    			<id>framework-repo</id>
    			<mirrorOf>framework-repo</mirrorOf>
    			<name>framework-repo</name>
    			 <url>https://raw.githubusercontent.com/wengsongwei8/maven-repo/master</url>  
    		</mirror>
    		
    		<mirror>
    			<id>aliyun</id>
    			<mirrorOf>central</mirrorOf>
    			<name>aliyun</name>
    			   <url>https://maven.aliyun.com/repository/jcenter</url>  
    		</mirror>
    ```
 特殊情况如果使用*，则把mirror中的mirrorOf属性改为：<mirrorOf>*,!my-maven-repo</mirrorOf>
   例如：
  ```xml
       		<mirror>
       			<id>aliyun</id>
       			<mirrorOf>*,!framework-repo</mirrorOf>
       			<name>sonatype</name>
       			   <url>https://maven.aliyun.com/repository/jcenter</url>  
       		</mirror>
  
``` 
因为
 ```xml
   <mirrorOf>*</mirrorOf>
``` 
   会覆盖掉所有的repository,导致项目中的repository不生效,所以需要以上配置
 * 还原数据库【数据库备份脚本在deploy/db目录】
 * 在配置文件：application-active.yml中修改数据库配置
 * 访问swagger-ui地址：http://127.0.0.1:8000/swagger-ui.html
 * 访问druid地址： http://127.0.0.1:8000/druid   admin/admin

   

##项目打包部署
* 打包命令：mvn clean assembly:assembly -Pdev 其中dev指的是开发环境,prod指的是生产环境,test指的是生产环境  
* 执行完打包命令以后，将生成一个.zip部署包
* 目录结构如下
    * bin启动和停止脚本
    * logs日志路径
    * conf 配置文件的位置
    * lib 依赖的jar
* 把zip部署上传到linux服务器，然后到bin目录下执行./start.sh即可


##常见问题
* 没安装lombok插件，导致部分代码显示报错（不影响正常运行）。

    * lombok插件可以在我们简化很多代码，安插也非常方便
    * idea中安装：打开IDEA的Setting –> 选择Plugins选项 –> 选择Browse repositories –> 搜索lombok –> 点击安装 –> 安装完成重启IDEA –> 安装成功
    * eclipse安装，先到官网下载lombok.jar，官网地址：https://projectlombok.org/download，
        将lombok.jar包拷贝到eclipse的安装目录下，并且在eclipse.ini文件中添加如下两行
    
    -Xbootclasspath/a:lombok.jar    --备注：如果没有这一行也是没问题的 <br/>
    -javaagent:lombok.jar

* 执行./start.sh 会报以下错误：
    bin/sh^M: bad interpreter: No such file or directory
  
    * 原因分析：.sh脚本在windows系统下用记事本文件编写的。不同系统的编码格式引起的。
    * 解决方法：vi start.sh，然后输入:set ff=unix ，然后保存退出即可
* 执行打包部署以后，然后再在idea或 eclipse启动项目，会报找不到数据库连接的错：
  
    * 原因分析：为了部署方便，在打包的时候避免把配置文件放在jar里，所以打包时会把配置方向往外放。所以在ide里启动就找不到对应的配置文件
    * 解决方法：再执行命令mvn install即可
