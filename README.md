# 项目前台后端实现注意事项如下
### 注意点1

根据阿里的标准：像PO、VO、DO、DTO这种类型的，不需要进行驼峰式命名处理

### 注意点2
配置文件与类上configureationproperties注解的关系与使用
/**
 * 使用注解@ConfigurationProperties映射
 * 通过注解@ConfigurationProperties(prefix="配置文件中的key的前缀")
 * 可以将配置文件中的配置自动与实体进行映射
 * 自动匹配，容器中的变量进行注入，适合较多的变量的配置
 * 注意：
 *      使用此注解进行映射时，需要通过set方式设置值，所有成员变量需要存在set方法
 */
使用此注解映射配置文件中的值，需要添加依赖
```pom
    <!--配置注解ConfigurationProperties的执行器，不配置不影响使用但是会出现警告，配置yml时会给提示，需要配置的成员变量-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
```

### 注意点3
RequestBody注解对consummer调用远程接口传参的重要性
请求时携带是通过set方式设置到对象中

远程传递的时json如果还是用springmvc中直接获取获取请求set设置值，程序
会忽略传递来的session。

原有请求方式：
get方式：http://ip:port/projectName?name=?&age=?
或post方式

接受数据方式：
```java
    /**
     * 将注册提交的用户数据进行保存
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultUtil<String> saveMember(@RequestBody MemberPO memberPO){
        try{
            memberMysqlService.saveMember(memberPO);
            return ResultUtil.ok(null);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                return ResultUtil.fail(CrowdConstant.MESSAGE_LOGIN_ACCt_ALREADY_IN_USE);
            }
            return ResultUtil.fail(e.getMessage());
        }
    }
```
我们使用原有请求传递可以不使用@RequestBody注解，但是如果使用远程传递参数这样传递给对象而不是单独的值，必须添加@RequestBody注解
否则会出现异常。

### 注意点4
DO、PO之间的转换关系的使用，要求设置的属性都具备set方法
BeanUtils.copyProperties(source,target);
此方法会将原对象中属性与target对象中属性相同的项，对应复制，通过set方法
```java
    //将membervo转换成memberpo，使用set设置值
    MemberPO memberPO = new MemberPO();
    BeanUtils.copyProperties(memberVO,memberPO);
```

### 注意点5：springsession原理与使用
springsession实现的改造是非侵入式的，不改变我们原有的使用方式，而是从底层接管tomcat的类。我们使用response、request、session，
都是通过接口调用使用。springsession将底层实现类替换从而实现从redis中进行存区查找seession

验证：我们可以在浏览器中看到cookie中携带的session不在式以sessioonid命名，说明底层实现被替换

**注意：存入 Session 域的实体类对象需要支持序列化**

使用步骤：添加依赖，添加配置项即可
1、保留原有添加session的方式，springseesion改变底层接口实现
```java
    @RequestMapping("/auth/member/do/login")
    public String login(ModelAndView mv, @RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd, HttpSession session){
        //省略部分代码.......
        //将用户数据存放到session中
        MemberVO memberVO = new MemberVO(user.getId(), loginacct, userpswd,user.getUsername(),user.getEmail());
        //保留原有方式
        session.setAttribute("membervo",memberVO);
        //此处应该重定向到会员中心页面
        return "redirect:/auth/member/to/center/page";

//        return mv;
    }
```
2、添加springsession依赖，
```pom
        <!--分布式数据一致性session解决方案：springsession-->
        <!-- 引入springboot&redis 整合场景-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- 引入springboot&springsession 整合场景-->
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
```

3、在application.yml中配置相关配置项，
springsession是在web服务器外通过一个数据库来保存公共信息，实现的数据库有很多，我们选择redis
```yaml
spring:
#    配置session共享配置项
  session:
    store-type: redis
#配置session共享相关配置项
  redis:
    host: redis服务器ip
    port: redis端口
    password: 如果配置redis密码，需要配置此项，否则不需要
```


### 注意点6：zuul环境下请求重定向导致主机ip改变
```java
    @RequestMapping("/auth/member/do/login")
    public String login(ModelAndView mv, @RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd, HttpSession session){
        ......
        //重定向后请求，不在使用zuul的ip，而是使用zuul解析出来的微服务主机电脑名+端口号；比如：YAN：4000
        return "redirect:/auth/member/to/center/page";
```
解决方案：
在zuul中配置文件增加配置项
```yml
# 配置zuul的路由规则
zuul:
  routes:
    auth:
      service-id: crowd-auth
      path: /**
  ignored-services: '*'
  sensitive-headers: '*'
# 此处解决后端服务重定向导致用户浏览的 host 变成 后端服务的 host 问题
  add-host-header: true
```

### 注意点7：
**分布式系统中，不是同一个项目的请求之间不能使用请求转发，必须使用请求重定向**
**通过Spring session进行传递共享信息**

### 注意点8：
session与spring session使用区别
session：session在内存中存放，我们从session中取出变量，进行修改，此时session由于是在内存中会自动改变
spring session：存放在redis中，我们从spring session中去取出变量后修改，redis中存放的数据并不会自动修改，其内存并不归属java
控制，所以需要我们再次进行重新设置，以保证下次数据取出的一致性。


### 注意点9：获取插入表中自动生成的主键id值----方式2
<insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" parameterType="com.yama.crowd.entity.po.ProjectPO" >
在配置文件中添加useGeneratedKeys="true" keyProperty="id"两个属性：
useGeneratedKeys="true"：表示使用自动生成的主键
keyProperty="id"：表示将自动生成的键值设置到对象中那个属性值中

### 注意点10：经过zuul使用不同的域名会导致session与cookie失效
window.location.href="http://localhost/project/launch/project/page";
但是我们使用的www.lover.com域名就会导致session失效，需要重新登陆。

### 注意点11：使用BeanUtils.copyProperties(memberVO,memberPO);类型不匹配
```java
    Long money;
    Integer money;
//两者类型不匹配导致，属性复制不成功
属性不一致导致money没有存储到数据库中
```
### 思考1：mybatis存储时，属性类型和列类型不一致是否可以存储成功。


### 问题点12：mybatis一对多，多对多查询问题
<association property="user" column="uid" javaType="user" select="com.yama.dao.IUserDao.findById"></association>
对于属性select所指的方法，可以不在mapper中进行声明，直接在xml配置文件中配置即可。

```xml
<!--
        注意：
        方式1：
        <resultMap id="accountuserMap" type="account">
            <id property="id" column="aid"></id>
            <result property="uid" column="uid"></result>
            <result property="money" column="money"></result>
            <association property="user" column="uid" javaType="user" select="com.yama.dao.IUserDao.findById"></association>
        </resultMap>
        这种方式进行获取联表查询数据，会将column属性中指定的列，作为参数传递给select中指定的方法，用来封装对象或者list集合后返回。
        每查询到一个id封装一次，最终返回一个集合。
        方式2：
         <resultMap id="roleuserMap" type="role">
            <id property="id" column="rid"></id>
            <result property="roleName" column="role_name"></result>
            <result property="roleDesc" column="role_desc"></result>
            <collection property="users" ofType="user" >
                <id property="id" column="id"></id>
                <result property="username" column="username"></result>
                <result property="birthday" column="birthday"></result>
                <result property="sex" column="sex"></result>
                <result property="address" column="address"></result>
            </collection>
        </resultMap>
        这种方式的就是将联表数据查询得到一个表，将表中的数据封装到但resultmap中，直接返回结果
-->
```


### 注意点15：mybatis字段映射问题
username------>userName;window可以忽略大小写，直接映射userName,但是linux系统不行，区分大小写
user_name----->userName;无法完成自动映射需要进行配置别名，或者resultMap


### 注意点16：当一个微服务中添加静态资源，其他微服务就可以不用添加了，因为可以通过zuul直接访问其他微服务的static文件

### 注意点17：对于远程服务接口中参数，对象需要添加@RequestBody注解，包装类等基本类型也需要添加@RequestParam注解

### 注意点18：sql语句中字段上可以进行算数：加、减、乘、除；运算注意使用。

## 注意点19：解决执行java -jar xxx.jar出现no main manifest attribute异常
* **注意：spring-boot-maven-plugin插件应该放到每个需要的项目中，不要放到父工程中会出现异常**
异常如下：
[root@VM-12-7-centos project-crowd-deploy]# java -jar crowdfunding01-member-eureka-1.1.0.RELEASE.jar 
no main manifest attribute, in crowdfunding01-member-eureka-1.1.0.RELEASE.jar
解决方案：
添加如下配置：
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
完整插件配置如下：
```pom
    <build>
        <plugins>
            <plugin>
                <!--问题点-->
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                            <goal>build-info</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

## 异常解决：
启动异常
```java
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'enableRedisKeyspaceNotificationsInitializer' defined in class path resource [org/springframework/boot/autoconfigure/session/RedisSessionConfiguration$SpringBootRedisHttpSessionConfiguration.class]: Invocation of init method failed; nested exception is org.springframework.data.redis.RedisSystemException: Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: ERR unknown command `CONFIG`, with args beginning with: `GET`, `notify-keyspace-events`, 
```

异常说明中出现关键词：CONFIG,Netty，表示远程连接redis出现异常，执行CONFIG命令失败。
解决方式：
启用CONFIG命令：
#rename-command CONFIG FRaqbC8wSA1XvpFVjCRGryWtIIZS2TRvpFVjCRG
