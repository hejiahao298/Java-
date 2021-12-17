# SpringMVC

Spring MVC 是目前主流的实现 MVC 设计模式的企业级开发框架，Spring 框架的一个子模块，无需整合，开发起来更加便捷。

![SpirngMVC](C:\Users\27600\Desktop\img\SpirngMVC.jpg)

### MVC设计模式

> MVC（Model–View–Controller）模式是软件工程中的一种**软件架构模式**，它把软件系统分为三个基本部分：**模型（Model）**、**视图（View）**和**控制器（Controller）**。

- 模型（Model）：封装业务数据 service，repository，entity
- 控制器（Controller）：处理业务逻辑 Servlet，Handler，Action；
- 视图（View）：通过业务逻辑将业务数据展示在View上 JSP，html，app客户端。

请求进入Java Web应用后，Controller接收该请求，进行业务逻辑处理，将最终的结果返回给用户(View+Model)



### SpringMVC的核心组件

- DispatcherServlet：前置控制器，是整个流程控制的核心，控制其它组件的执行，进行统一的调度，降低组件之间的耦合性，相当于总指挥
- Handler：处理器，完成具体的业务逻辑，相当于Servlet或Action
- HandlerMapping：DispatcherServlet接收到请求后，通过HandlerMapping将不同的请求映射到不同的Handler。
- HandlerInterceptor：处理器拦截器，是一个接口，如果需要完成一些拦截处理，可以实现该接口。
- HandlerExecutionChain：处理器执行链，包括两部分内容：Handler和HandlerInterceptor（系统会有一个默认的 HandlerInterceptor，如果需要额外设置拦截，可以添加拦截器）。
- HandlerAdapter：处理器适配器，Handler执行业务方法之前，需要进行一系列的操作，包括表单数据的验证，数据类型的转换，将表单数据封装到JavaBean等，这些操作都是由 HandlerApater 来完成，开发者只需将注意力集中业务逻辑的处理上，DispatcherServlet 通过 HandlerAdapter 执行不同的 Handler。
- ModelAndView：装载了模型数据和视图信息，作为Handler的处理结果，返回给DisPactherServlet。
- ViewResolver：视图解析器，DisPatcherServlet通过它将逻辑视图解析为物理视图，最终将渲染结果响应给客户端。



### SpringMVC的工作流程

1. 客户端请求被DispatcherServlet接收
2. 根据HandlerMapping映射到Handler
3. 生成Handler和HandlerInterceptor
4. Handler和HandlerInterceptor以HandlerExecutionChain的形式一并返回给DispatcherServlet
5. DispatcherServlet通过HandlerAdapter调用Handler的方法完成业务逻辑处理
6. Handler返回一个ModeAndView给DispatcherServlet
7. DispatcherServlet 将获取的 ModelAndView 对象传给 ViewResolver 视图解析器，将逻辑视图解析为物理视图 View
8. ViewResovler 返回一个 View 给 DispatcherServlet
9. DispatcherServlet 根据 View 进行视图渲染（将模型数据 Model 填充到视图 View 中）
10. DispatcherServlet 将渲染后的结果响应给客户端

![1139441444-0](C:\Users\27600\Desktop\img\1139441444-0.png)

Spring MVC 流程非常复杂，实际开发中很简单，因为大部分的组件不需要开发者创建、管理，只需要通过配置文件的方式完成配置即可，真正需要开发者进行处理的只有 Handler 、View。



# springMVC核心组件的配置

## DispatcherServlet：前置控制器

Spring MVC 是基于 Servlet 的，DispatcherServlet 是整个 Spring MVC 框架的核心，主要负责截获请求并将其分派给相应的处理器处理。所以配置 Spring MVC，首先要定义 DispatcherServlet。跟所有 Servlet 一样，用户必须在 web.xml 中进行配置。

### 1）定义DispatcherServlet

在开发 Spring MVC 应用时需要在 web.xml 中部署 DispatcherServlet，代码如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    version="3.0">
    <display-name>springMVC</display-name>
    <!-- 部署 DispatcherServlet -->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 表示容器再启动时立即加载servlet -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- 处理所有URL -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```



## Handler处理器 和 HandlerMapping处理映射器

@Controller 注解用于声明某类的实例是一个控制器。

@RequestMapping:一个控制器内有多个处理请求的方法

```java
@Controller
@RequestMapping(value = "/login")
public class login{
    @RequestMapping(value = "/user")
    public String user(){
        return "user"
    }
}
```



## SpringMVC拦截器

> 在系统中，经常需要在处理用户请求之前和之后执行一些行为，例如检测用户的权限，或者将请求的信息记录到日志中，即平时所说的“权限检测”及“日志记录”。当然不仅仅这些，所以需要一种机制，拦截用户的请求，在请求的前后添加处理逻辑。

Spring MVC 提供了 `Interceptor` 拦截器机制，用于请求的预处理和后处理。通过实现 HandlerInterceptor 接口或继承 HandlerInterceptor 接口的实现类（例如 HandlerInterceptorAdapter）来定义；

**HandlerInterceptor接口的方法**

- afterCompletion：在控制器的处理请求方法执行完成后执行，即视图渲染结束之后执行，通过此方法实现一些资源清理、记录日志信息等工作。
- postHandle：在控制器的处理请求方法调用之后，解析视图之前执行，通过此方法对请求域中的模型和视图做进一步的修改。
- preHandle：在控制器的处理请求方法调用之前执行，其返回值表示是否中断后续操作，返回 true 表示继续向下执行，返回 false 表示中断后续操作。

在xml中配置拦截器

```xml
<!-- 配置一组拦截器 -->
<mvc:interceptors>
    <!-- 配置一个全局拦截器，拦截所有请求 -->
    <bean class="net.biancheng.interceptor.TestInterceptor" /> 
    
    <!-- 子拦截器1-->
    <mvc:interceptor>
        <!-- 配置拦截器作用的路径 -->
        <mvc:mapping path="/**" />
        <!-- 配置不需要拦截作用的路径 -->
        <mvc:exclude-mapping path="" />
        <!-- 定义<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
        <bean class="net.biancheng.interceptor.Interceptor1" />
    </mvc:interceptor>
    
    <!-- 子拦截器2-->
    <mvc:interceptor>
        <!-- 配置拦截器作用的路径 -->
        <mvc:mapping path="/gotoTest" />
        <!-- 定义在<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
        <bean class="net.biancheng.interceptor.Interceptor2" />
    </mvc:interceptor>
</mvc:interceptors>
```

具体使用

```java
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String url = request.getRequestURI();
        if(url.indexOf("/toLogin")>=0 || url.indexOf("/login")>=0){
            return true;
        }
        
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        if(obj != null) return true;
            request.setAttribute("msg","还没登录，请先登录！");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        return false;     
    }
    
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }
}
```





## 视图解析器 ：InternalResourceViewResolver

> URLBasedViewResolver
>
> InternalResourceViewResolver ：`常用`-为“内部资源视图解析器”，是日常开发中最常用的视图解析器类型。它是 URLBasedViewResolver 的子类，拥有 URLBasedViewResolver 的一切特性。能自动将返回的视图名称解析为 InternalResourceView 类型的对象。
> 
> FreeMarkerViewResolver
> 

- 下面以InternalResoureViewResolver举例

在springmvc.xml中的配置 - 通过指定前后缀的方式将逻辑视图解析为模型视图

```xml
<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!--前缀-->
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <!--后缀-->
    <property name="suffix" value=".jsp"/>  
 </bean>
```



## 类型转换器 Converter<S, T>

> 在springmvc中有很多内置的类型转换器，当前端传来请求带有参数时，HandlerAdapter会自动根据转换器转换成相应的类型。但有些特殊的转换还需要手动转换。

在xml中的配置

```xml
<mvc:annotation-driven conversion-service="conversionService" />
<!--注册类型转换器UserConverter -->
<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="net.biancheng.converter.UserConverter" />
        </list>
    </property>
</bean>
```

String类型转换为User类型的转换器

```java
@Component
public class UserConverter implements Converter<String,User>{
    @Override
    public User convert(String source){
        User user = new User();
        String strvalues[] = source.split("-");
        if(strvalues != null && strvalues.length == 3){
            user.setName(strvalues[0]);
            user.setAge(Integer.parseInt(strvalues[1]);
            user.setHeight(Double.parseDouble(strvalues[2]);
            return user;
        }else{
            throw new IllegalArgumentException(String.format("类型转换失败， 需要格式'编程帮, 18,1.85',但格式是[% s ] ", source));
        }
    }
}
```

### 数据格式化Formatter<T>

>  Formatter<T> 与 Converter<S, T> 一样，也是一个可以将一种数据类型转换成另一种数据类型的接口。不同的是，Formatter 的源类型必须是 String 类型，而 Converter 的源类型可以是任意数据类型
>
> Spring MVC 提供了几个内置的格式化转换器，具体如下。
>
> - NumberFormatter：实现 Number 与 String 之间的解析与格式化。
> - CurrencyFormatter：实现 Number 与 String 之间的解析与格式化（带货币符号）。
> - PercentFormatter：实现 Number 与 String 之间的解析与格式化（带百分数符号）。
> - DateFormatter：实现 Date 与 String 之间的解析与格式化。

`自定义格式化转换器`

- String类型转换为Date类型，<u>（Converter同样也能做到）</u>

在xml中的配置：和Converter大同小异

```xml
<!--注册MyFormatter -->
<bean id="conversionService"
    class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <property name="formatters">
        <set>
            <bean class="net.biancheng.formatter.MyFormatter" />
        </set>
    </property>
</bean>
<mvc:annotation-driven conversion-service="conversionService" />
```

自定义格式转换器

```java
@Component
public class MyFormatter implements Formatter<Date>{
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public String print(Date object,Locale arg1){
        return dateFormat.format(object);
    }
    
    public Date parse(string source,Locale arg1) throws ParseException{
        return dateFormat.parse(source)
    }
}
```



## SpringMVC拦截器

> 在系统中，经常需要在处理用户请求之前和之后执行一些行为，例如检测用户的权限，或者将请求的信息记录到日志中，即平时所说的“权限检测”及“日志记录”。当然不仅仅这些，所以需要一种机制，拦截用户的请求，在请求的前后添加处理逻辑。

Spring MVC 提供了 `Interceptor` 拦截器机制，用于请求的预处理和后处理。通过实现 HandlerInterceptor 接口或继承 HandlerInterceptor 接口的实现类（例如 HandlerInterceptorAdapter）来定义；

**HandlerInterceptor接口的方法**

- afterCompletion：在控制器的处理请求方法执行完成后执行，即视图渲染结束之后执行，通过此方法实现一些资源清理、记录日志信息等工作。
- postHandle：在控制器的处理请求方法调用之后，解析视图之前执行，通过此方法对请求域中的模型和视图做进一步的修改。
- preHandle：在控制器的处理请求方法调用之前执行，其返回值表示是否中断后续操作，返回 true 表示继续向下执行，返回 false 表示中断后续操作。

在xml中配置拦截器

```xml
<!-- 配置一组拦截器 -->
<mvc:interceptors>
    <!-- 配置一个全局拦截器，拦截所有请求 -->
    <bean class="net.biancheng.interceptor.TestInterceptor" /> 
    
    <!-- 子拦截器1-->
    <mvc:interceptor>
        <!-- 配置拦截器作用的路径 -->
        <mvc:mapping path="/**" />
        <!-- 配置不需要拦截作用的路径 -->
        <mvc:exclude-mapping path="" />
        <!-- 定义<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
        <bean class="net.biancheng.interceptor.Interceptor1" />
    </mvc:interceptor>
    
    <!-- 子拦截器2-->
    <mvc:interceptor>
        <!-- 配置拦截器作用的路径 -->
        <mvc:mapping path="/gotoTest" />
        <!-- 定义在<mvc:interceptor>元素中，表示匹配指定路径的请求才进行拦截 -->
        <bean class="net.biancheng.interceptor.Interceptor2" />
    </mvc:interceptor>
</mvc:interceptors>
```

具体使用

```java
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String url = request.getRequestURI();
        if(url.indexOf("/toLogin")>=0 || url.indexOf("/login")>=0){
            return true;
        }
        
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        if(obj != null) return true;
            request.setAttribute("msg","还没登录，请先登录！");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        return false;     
    }
    
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }
}
```



## springMVC 需要的jar包

```xml
   <dependencies>
<!--    servlet包    -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

<!--    SpringMVC包    -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.11.RELEASE</version>
        </dependency>

<!--     lombok@Date包    -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.10</version>
        </dependency>
        
    </dependencies>
```





## SpringMVC传递参数

Spring MVC Controller 接收请求参数的方式有很多种，有的适合 get 请求方式，有的适合 post 请求方式，有的两者都适合。主要有以下几种方式：

- 通过实体 Bean 接收请求参数
- 通过处理方法的形参接收请求参数
- 通过 HttpServletRequest 接收请求参数
- 通过 @PathVariable 接收 URL 中的请求参数
- 通过 @RequestParam 接收请求参数
- 通过 @ModelAttribute 接收请求参数

### 通过实体Bean接收请求参数

```java
@RequestMapping("/login")
public String login(User user, Model model) {
    if ("张三".equals(user.getName())
            && "123456".equals(user.getPwd())) {
       
        model.addAttribute("message", "登录成功");
        return "index"; // 登录成功，跳转到 index.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



### 通过处理方法的形参接收请求参数

```java
@RequestMapping("/login")
public String login(String name, String pwd, Model model) {
    if ("张三".equals(user.getName())
            && "123456".equals(user.getPwd())) {
       
        model.addAttribute("message", "登录成功");
        return "main"; // 登录成功，跳转到 main.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



### 通过HttpServletRequest接收请求参数

```java
@RequestMapping("/login")
public String login(HttpServletRequest request, Model model) {
    String name = request.getParameter("name");
    String pwd = request.getParameter("pwd");
   
    if ("张三".equals(name)
            && "123456".equals(pwd)) {
       
        model.addAttribute("message", "登录成功");
        return "main"; // 登录成功，跳转到 main.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



### 通过@PathVariable接收URL中的请求参数

自动将 URL 中的模板变量 {name} 和 {pwd} 绑定到通过 @PathVariable 注解的同名参数上

```java
@RequestMapping("/login/{name}/{pwd}")
public String login(@PathVariable String name, @PathVariable String pwd, Model model) {
   
    if ("张三".equals(name)
            && "123456".equals(pwd)) {
       
        model.addAttribute("message", "登录成功");
        return "main"; // 登录成功，跳转到 main.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



### 通过@RequestParam接收请求参数

在方法入参处使用 @RequestParam 注解指定其对应的请求参数。

@RequestParam 有以下三个参数：

- value：参数名
- required：是否必须，默认为 true，表示请求中必须包含对应的参数名，若不存在将抛出异常
- defaultValue：参数默认值
- 该方式与“通过处理方法的形参接收请求参数”部分的区别如下：当请求参数与接收参数名不一致时，“通过处理方法的形参接收请求参数”不会报 404 错误，而“通过 @RequestParam 接收请求参数”会报 404 错误。

```java
@RequestMapping("/login")
public String login(@RequestParam String name, @RequestParam String pwd, Model model) {
   
    if ("bianchengbang".equals(name)
            && "123456".equals(pwd)) {
       
        model.addAttribute("message", "登录成功");
        return "main"; // 登录成功，跳转到 main.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



### 通过@ModelAttribute接收请求参数

@ModelAttribute 注解用于将多个请求参数封装到一个实体对象中，从而简化数据绑定流程，而且自动暴露为模型数据，在视图页面展示时使用。

而“通过实体 Bean 接收请求参数”中只是将多个请求参数封装到一个实体对象，并不能暴露为模型数据（需要使用 model.addAttribute 语句才能暴露为模型数据，数据绑定与模型数据展示后面教程中会讲解）。

```java
@RequestMapping("/login")
public String login(@ModelAttribute("user") User user, Model model) {
   
    if ("张三".equals(name)
            && "123456".equals(pwd)) {
       
        model.addAttribute("message", "登录成功");
        return "main"; // 登录成功，跳转到 main.jsp
    } else {
        model.addAttribute("message", "用户名或密码错误");
        return "login";
    }
}
```



## SpringMVC重定向和转发

Spring MVC 请求方式分为转发、重定向 2 种，分别使用 forward 和 redirect 关键字在 controller 层进行处理。（通过视图解析器，默认为转发）

```java
@RequestMapping(value = "/login")
    public String login(String password,String username,String type,Model model){

        if(type.equals("reader")){
            if(password.equals("123") && username.equals("张三")){
                model.addAttribute("message",username);
                return "forward/index.jsp";        //通过视图解析器转换成jsp
            }else
                return "redirect:/login.jsp";     //重定向，默认的解析器为转发
```



## SpringMVC数据的绑定

数据绑定：在后端的业务方法中直接获取客户端 HTTP 请求中的参数，将请求参数映射到业务方法的形参中，Spring MVC 中数据绑定的工作是由 HandlerAdapter 来完成的。

- 基本数据类型
- 包装类
- 数组
- Map
- JSPON

### 基本数据类型

```java
@RequestMapping("/baseType")
@ResponseBody                 // 表示 Spring MVC 会直接将业务方法的返回值响应给客户端
public String baseType(int id){       //接收前端传来的id参数
    return id+"";
}
```

### 包装类(可以接收空值)

```java
@RequestMapping("/packageType")
@ResponseBody
public String packageType(@RequestParam(value = "num",required = false,defaultValue = "0") Integer id){
    return id+"";
}
```

### 数组

```java
@RestController
@RequestMapping("/data")
public class DataBindHandler {
    @RequestMapping("/array")
    public String array(String[] name){         //接收多个name参数请求
        String str = Arrays.toString(name);
        return str;
    }
}
```

### List

```java
//entity
@Data
public class UserList {
    private List<User> users;
}
```

```js
//JSP
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/data/list" method="post">
        用户1编号：<input type="text" name="users[0].id"/><br/>
        用户1名称：<input type="text" name="users[0].name"/><br/>
        用户2编号：<input type="text" name="users[1].id"/><br/>
        用户2名称：<input type="text" name="users[1].name"/><br/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
```

```java
//Controller
@RequestMapping("/list")
public String list(UserList userList){
    StringBuffer str = new StringBuffer();
    for(User user:userList.getUsers()){
        str.append(user);
    }
    return str.toString();
}
```

### Map

```java
//entity
@Data
public class UserMap {
    private Map<String,User> users;
}
```

```java
//Controller
@RequestMapping("/map")
public String map(UserMap userMap){
    StringBuffer str = new StringBuffer();
    for(String key:userMap.getUsers().keySet()){
        User user = userMap.getUsers().get(key);
        str.append(user);
    }
    return str.toString();
}
```

```java
//JSP
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/data/map" method="post">
        用户1编号：<input type="text" name="users['a'].id"/><br/>
        用户1名称：<input type="text" name="users['a'].name"/><br/>
        用户2编号：<input type="text" name="users['b'].id"/><br/>
        用户2名称：<input type="text" name="users['b'].name"/><br/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
```

### JOSN

客户端发生 JSON 格式的数据，直接通过 Spring MVC 绑定到业务方法的形参中。

```xml
<!-- 处理 Spring MVC 无法加载静态资源，在 web.xml 中添加配置即可。 -->
<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.js</url-pattern>
</servlet-mapping>
```

```jsp
<!-- JSP -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript">
        $(function(){
           var user = {
               "id":1,
               "name":"张三"
           };
           $.ajax({
               url:"/data/json",
               data:JSON.stringify(user),
               type:"POST",
               contentType:"application/json;charset=UTF-8",
               dataType:"JSON",
               success:function(data){
                   alter(data.id+"---"+data.name);
               }
           })
        });
    </script>
</head>
<body>

</body>
</html>
```

```java
//Controller
@RequestMapping("/json")
public User json(@RequestBody User user){
    System.out.println(user);
    user.setId(6);
    user.setName("张六");
    return user;
}
```

```xml
<!--Spring MVC 中的 JSON 和 JavaBean 的转换需要借助于 fastjson，pom.xml 引入相关依赖。-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.32</version>
</dependency>
```

```xml
<!-- 配置fastjson,在SpringMVC中 -->
<bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4"></bean>
```



## SpringMVC 模型数据解析

JSP 四大作用域对应的内置对象：pageContext、request、session、application。

模型数据的绑定是由 ViewResolver 来完成的，实际开发中，我们需要先添加模型数据，再交给 ViewResolver 来绑定。

Spring MVC 提供了以下几种方式添加模型数据：

- Map
- Model
- ModelAndView
- @SessionAttribute
- @ModelAttribute

> 将模式数据绑定到 request 对象。
>
> Map , Model , ModelAndView

```java
//从springIOC容器中获取reader对象
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
    Reader reader = (Reader) applicationContext.getBean("reader");
```

```jsp
<!-- JSP -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${requestScope.reader}
</body>
</html>
```



1. **Map**

```java
@RequestMapping("/map")
    public String Map(){
        Map<String,Reader> map = new HashMap<>();
        map.put("reader",reader);      //JSP可以通过el表达式从request中拿出
        return "view";                 //通过视图解析器
    }
```



2. **Model**

```java
@RequestMapping("/model")
    public String Model(Model model){
        model.addAttribute("reader",reader);     //model底层就是一个hashMap
        return "view";
    }
```



**ModelAndView**

```java
@RequestMapping("/modelandview1")
    public ModelAndView modelAndView1(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reader",reader);
        modelAndView.setViewName("view");
        return modelAndView;
    }

@RequestMapping("/modelandview4")
    public ModelAndView modelAndView4(){
        ModelAndView modelAndView = new ModelAndView("view","reader",reader);
        return modelAndView;
    }
```



```java
//Model源码
//1.无参构造，通过addObject和setView(setViewName)添加数据
 public ModelAndView() {
    }

//2. ViewName参数，相当于调用了setViewName()
    public ModelAndView(String viewName) {
        this.view = viewName;
    }

//3. view参数，需要实例化一个View对象，View view = new InternalResourceView("/view.jsp");
    public ModelAndView(View view) {
        this.view = view;
    }

/*4.传入一个ViewName对象，和Map对象，需要实例化一个Map对象传入。
*   getModeMap()获取一个ModelMap对象，
*   addAllAtrributes(model) , 底层是hashMap的putAll()方法
*/
    public ModelAndView(String viewName, @Nullable Map<String, ?> model) {
        this.view = viewName;
        if (model != null) {
            this.getModelMap().addAllAttributes(model);
        }

    }

//5.传入一个View对象和一个Map对象，同理
    public ModelAndView(View view, @Nullable Map<String, ?> model) {
        this.view = view;
        if (model != null) {
            this.getModelMap().addAllAttributes(model);
        }

    }

//6. 传入一个逻辑视图名ViewName，和一个Http请求状态
    public ModelAndView(String viewName, HttpStatus status) {
        this.view = viewName;
        this.status = status;
    }

/*7.  传入一个逻辑视图名ViewName, 参数modelName，modelObject
*  addObject(String attributeName, Object attributeValue) {
*            this.getModelMap().addAttribute(attributeName, attributeValue);
*            }           
*     addAttribute底层调用的是HashMap的put()方法
*/
    public ModelAndView(String viewName, String modelName, Object modelObject) {
        this.view = viewName;
        this.addObject(modelName, modelObject);
    }

//8. 传入一个View对象，modelName，modelObject参数为map的（key，value）
    public ModelAndView(View view, String modelName, Object modelObject) {
        this.view = view;
        this.addObject(modelName, modelObject);
    }
```



## SpringMVC REST风格

REST（Representational State Transfer）即表述性转移，是目前最流行的一种软件架构风格。它结构清晰、易于理解、有较好的扩展性。

> REST 概念较为复杂，我们不过多解释，大家简单了解 Spring MVC 的 REST 风格的简单使用即可。

Spring REST 风格可以简单理解为：使用 URL 表示资源时，每个资源都用一个独一无二的 URL 来表示，并使用 HTTP 方法表示操作，即准确描述服务器对资源的处理动作（GET、POST、PUT、DELETE），实现资源的增删改查。

- GET：表示获取资源
- POST：表示新建资源
- PUT：表示更新资源
- DELETE：表示删除资源


下面举例说明 REST 风格的 URL 与传统 URL 的区别。

/userview.html?id=12  VS   /user/view/12
/userdelete.html?id=12  VS   /user/delete/12
/usermodify.html?id=12  VS   /user/modify/12

但是也有弊端，对于国内项目，URL 参数有时会传递中文，而中文乱码是一个令人头疼的问题，所以我们应该根据实际情况进行灵活处理。很多网站都是传统 URL 风格与 REST 风格混搭使用。

## 示例

由于 HTTP 不支持 PUT 和 DELETE 请求，所以需要将 DELETE 和 PUT 请求转换成 POST 请求，在 web.xml 中配置过滤器 HiddenHttpMethodFilter。

```xml
<!-- HiddenHttpMethodFilter过滤器可以将POST请求转化为put请求和delete请求! -->
<filter>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

新建 rest.jsp 代码如下。

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST风格</title>
</head>
<body>
    <h4>发送GET请求</h4>
    <a href=" user/1">GET</a>
    
    <h4>发送POST请求</h4>
    <form action="user/1" method="post"></form>
    
    <!-- 发送PUT和DELETE请求时，需要添加一个隐藏域 -->
    <h4>发送PUT请求</h4>
    <form action=" user/1" method="post"></form>
    
    <h4>发送DELETE请求</h4>
    <input type="hidden" name="_method" value="DELETE" />
    <form action=" user/1" method="post"></form>
</body>
</html>
```

下面通过 @RequestMapping 映射请求中的 method 参数实现四种请求方式的调用，UserController 代码如下。

```java
@GetMapping(value = "")
@PostMapping(value = "")
@PutMapping(value = "")
@DeletMapping(value = "")
```



## 表单标签库

我们在进行 Spring MVC 项目开发时，一般会使用 EL 表达式和 JSTL 标签来完成页面视图的开发。其实 Spring 也有自己的一套表单标签库，通过 Spring 表单标签，可以很容易地将模型数据中的命令对象绑定到 HTML 表单元素中。下面我们就通过一个示例来演示该标签库的用法。

首先和 JSTL 标签的使用方法相同，在使用 Spring 表单标签之前，必须在 JSP 页面开头处声明 taglib 指令，指令代码如下。

<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form"%>


常用的 Spring 表单标签如下表所示。

| 名称         | 作用                                                   |
| ------------ | ------------------------------------------------------ |
| form         | 渲染表单元素                                           |
| input        | 输入框组件标签，渲染 <input type="text"/> 元素         |
| password     | 密码框组件标签，渲染 <input type="password"/> 元素     |
| hidden       | 隐藏框组件标签，渲染 <input type="hidden"/> 元素       |
| textarea     | 多行输入框组件标签，渲染 textarea 元素                 |
| checkbox     | 复选框组件标签，渲染一个 <input type="checkbox"/> 元素 |
| checkboxes   | 渲染多个 <input type="checkbox"/> 元素                 |
| radiobutton  | 单选框组件标签，渲染一个 <input type="radio"/> 元素    |
| radiobuttons | 渲染多个 <input type="radio"/> 元素                    |
| select       | 下拉列表组件标签，渲染一个选择元素                     |
| option       | 渲染一个选项元素                                       |
| options      | 渲染多个选项元素                                       |
| errors       | 显示表单数据校验所对应的错误信息                       |

以上标签基本都拥有以下属性。

- path：属性路径，表示表单对象属性，如 userName、userCode 等。
- cssClass：表单组件对应的 CSS 样式类名。
- cssErrorClass：当提交表单后报错（服务端错误），采用的 CSS 样式类。
- cssStyle：表单组件对应的 CSS 样式。
- htmlEscape：绑定的表单属性值是否要对 HTML 特殊字符进行转换，默认为 true。



## 文件的上传与下载

Spring MVC 框架的文件上传基于 commons-fileupload 组件，并在该组件上做了进一步的封装，简化了文件上传的代码实现，取消了不同上传组件上的编程差异。

#### MultipartResolver接口

在 Spring MVC 中实现文件上传十分容易，它为文件上传提供了直接支持，即 MultpartiResolver 接口。MultipartResolver 用于处理上传请求，将上传请求包装成可以直接获取文件的数据，从而方便操作。

MultpartiResolver 接口有以下两个实现类：

- StandardServletMultipartResolver：使用了 Servlet 3.0 标准的上传方式。
- CommonsMultipartResolver：使用了 Apache 的 commons-fileupload 来完成具体的上传操作。


MultpartiResolver 接口具有以下方法。

| 名称                              | 作用                                    |
| --------------------------------- | --------------------------------------- |
| byte[] getBytes()                 | 以字节数组的形式返回文件的内容          |
| String getContentType()           | 返回文件的内容类型                      |
| InputStream getInputStream()      | 返回一个InputStream，从中读取文件的内容 |
| String getName()                  | 返回请求参数的名称                      |
| String getOriginalFillename()     | 返回客户端提交的原始文件名称            |
| long getSize()                    | 返回文件的大小，单位为字节              |
| boolean isEmpty()                 | 判断被上传文件是否为空                  |
| void transferTo(File destination) | 将上传文件保存到目标目录下              |



## 数据表单的验证

JSR 303 是 Java 为 Bean 数据合法性校验所提供的标准框架。JSR 303 通过在 Bean 属性上标注类似于 @NotNull、@Max 等标准的注解指定校验规则，并通过标准的验证接口对 Bean 进行验证。可以通过 https://jcp.org/en/jsr/detail?id=303 查看详细内容并下载 JSR 303 Bean Validation。

JSR 303 不需要编写验证器，它定义了一套可标注在成员变量、属性方法上的校验注解，如下表所示。

| 名称                         | 说明                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| @Null                        | 被标注的元素必须为 null                                      |
| @NotNull                     | 被标注的元素必须不为 null                                    |
| @AssertTrue                  | 被标注的元素必须为 true                                      |
| @AssertFalse                 | 被标注的元素必须为 false                                     |
| @Min(value)                  | 被标注的元素必须是一个数字，其值必须大于等于指定的最小值     |
| @Max(value)                  | 被标注的元素必须是一个数字，其值必须小于等于指定的最大值     |
| @DecimalMax(value)           | 被标注的元素必须是一个数字，其值必须大于等于指定的最大值     |
| @DecimalMin(value)           | 被标注的元素必须是一个数字，其值必须小于等于指定的最小值     |
| @size                        | 被标注的元素的大小必须在指定的范围内                         |
| @Digits（integer，fraction） | 被标注的元素必须是一个数字，其值必须在可接受的范围内；integer 指定整数精度，fraction 指定小数精度 |
| @Past                        | 被标注的元素必须是一个过去的日期                             |
| @Future                      | 被标注的元素必须是一个将来的日期                             |
| @Pattern(value)              | 被标注的元素必须符合指定的正则表达式                         |

## 示例

本节示例基于《[@Controller和@RequestMapping注解](http://c.biancheng.net/spring_mvc/controller-requestmapping.html)》一节中的 springmvcDemo2 实现。

#### 1. 导入依赖

pom.xml 文件中添加以下代码。

```xml
<!-- 数据校验 -->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.1.0.Final</version>
</dependency>
<dependency>
    <groupId>org.jboss.logging</groupId>
    <artifactId>jboss-logging</artifactId>
    <version>3.1.0.CR2</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.1.0.Final</version>
</dependency>
```

#### 2. 创建实体类

创建 User 实体类，代码如下。

```java
public class User {
    @NotNull(message = "用户id不能为空")
    private Integer id;
    @NotNull
    @Length(min = 2, max = 8, message = "用户名不能少于2位大于8位")
    private String name;
    @Email(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]", message = "邮箱格式不正确")
    private String email;
    /** 省略setter和getter方法*/
}
```

#### 3. 创建JSP页面

创建 addUser.jsp，代码如下。

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/validate" method="post">
        用户id：<input type="text" name="id" />
        <br>
        用户名：<input type="text" name="name" />
        <br>
        邮箱：<input type="text" name="email" />
        <br>
        <input type="submit" value="提交" />
    </form>
</body>
</html>
```

#### 4. 创建控制器

创建 UserController 控制器类，代码如下。

```java

@Controller
public class UserController {
    @RequestMapping("/validate")
    public String validate(@Valid User user, BindingResult result) {
        // 如果有异常信息
        if (result.hasErrors()) {
            // 获取异常信息对象
            List<ObjectError> errors = result.getAllErrors();
            // 将异常信息输出
            for (ObjectError error : errors) {
                System.out.println(error.getDefaultMessage());
            }
        }
        return "index";
    }
    @RequestMapping(value = "/addUser")
    public String add() {
        return "addUser";
    }
}
```





## 异常处理

Spring MVC 有以下 3 种处理异常的方式：

1. 使用 Spring MVC 提供的简单异常处理器 SimpleMappingExceptionResolver。
2. 实现 Spring 的异常处理接口 HandlerExceptionResolver，自定义自己的异常处理器。
3. 使用 @ExceptionHandler 注解实现异常处理

## 1. @ExceptionHandler

局部异常处理仅能处理指定 Controller 中的异常。

**示例 1：**下面使用 @ExceptionHandler 注解实现。定义一个处理过程中可能会存在异常情况的 testExceptionHandle 方法。

```java
@RequestMapping("/testExceptionHandle")
public String testExceptionHandle(@RequestParam("i") Integer i) {
    System.out.println(10 / i);
    return "success";
}
```

显然，当 i=0 时会产生算术运算异常。

下面在同一个类中定义处理异常的方法。

```java
@ExceptionHandler({ ArithmeticException.class })
public String testArithmeticException(Exception e) {
    System.out.println("打印错误信息 ===> ArithmeticException:" + e);
    // 跳转到指定页面
    return "error";
}
```

注意：该注解不是加在产生异常的方法上，而是加在处理异常的方法上。

异常页面 error.jsp 代码如下。

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误页面</title>
</head>
<body>
    发生算术运算异常，请重新输出数据！
</body>
</html>
```

> 被 @ExceptionHandler 标记为异常处理方法，不能在方法中设置别的形参。但是可以使用 ModelAndView 向前台传递数据。

使用局部异常处理，仅能处理某个 Controller 中的异常，若需要对所有异常进行统一处理，可使用以下两种方法。

## 2. HandlerExceptionResolver

Spring MVC 通过 HandlerExceptionResolver 处理程序异常，包括处理器异常、数据绑定异常以及控制器执行时发生的异常。HandlerExceptionResolver 仅有一个接口方法，源码如下。

```java
public interface HandlerExceptionResolver {
    @Nullable
    ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
}
```

创建一个 HandlerExceptionResolver 接口的实现类 MyExceptionHandler，代码如下。

```java
public class MyExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            Exception arg3) {
        Map<String, Object> model = new HashMap<String, Object>();
        // 根据不同错误转向不同页面（统一处理），即异常与View的对应关系
        if (arg3 instanceof ArithmeticException) {
            return new ModelAndView("error", model);
        }
        return new ModelAndView("error-2", model);
    }
}
```









































