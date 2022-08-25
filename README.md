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