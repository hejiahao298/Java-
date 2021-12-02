### Filter

### 源码

```java
public interface Filter {
    default public void init(FilterConfig filterConfig) throws ServletException {}
    
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException;

    default public void destroy() {}
}
```



### 功能：

1. ⽤来拦截传⼊的请求和传出的响应。
2. 修改或以某种⽅式处理正在客户端和服务端之间交换的数据流。

### 使用

与使⽤ Servlet 类似，Filter 是 Java WEB 提供的⼀个接⼝，开发者只需要⾃定义⼀个类并且实现该接⼝ 即可。

```Java
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/*
* 给Servlet设置编码格式
* */
@WebFilter("index.jsp")
public class CharsetFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        chain.doFilter(request,response);
    }
}
```

配置方法和Servlet相似，可以在xml中配置，也可以使用注解的方式

1. web.xml中配置

```xml
<filter>
 <filter-name>charcater</filter-name>
 <filter-class>com.southwind.filter.CharacterFilter</filter-class>
</filter>
<filter-mapping>
 <filter-name>charcater</filter-name>
 <url-pattern>/login</url-pattern>
 <url-pattern>/test</url-pattern>
</filter-mapping>

```



2. 使用注解的方式

   > @WebFilter("/login /test")



### Filter的生命周期

当 Tomcat 启动时，通过反射机制调⽤ Filter 的⽆参构造函数创建实例化对象，同时调⽤ init ⽅法实现 初始化，doFilter ⽅法调⽤多次，当 Tomcat 服务关闭的时候，调⽤ destory 来销毁 Filter 对象。

> init方法: 只调⽤⼀次，当 Filter 的实例化对象创建完成之后调⽤。
>
> doFilter：调⽤多次，访问 Filter 的业务逻辑都写在 Filter 中
>
> destory：只调⽤⼀次，Tomcat 关闭时调⽤。



## Filter应用场景

### 1. 同一处理中文乱码

```java
/*
* 给Servlet处理中文乱码
* */
@WebFilter("index.jsp")
public class CharsetFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        chain.doFilter(request,response);
    }
}
```



### 2. 屏蔽敏感词汇

```java
/*
* 屏蔽敏感词汇的Filter
* */
@WebFilter("/index.jsp")
public class SenseWordFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String Word = request.getParameter("Word");
        String str = Word.replaceAll("敏感词","***");
        request.setAttribute("str",str);
        chain.doFilter(request,response);
    }
}
```



### 3. 设置用户访问权限

```java
/*
*    拦截用户直接访问首页，需要登录后访问
* */
@WebFilter("/index.jsp")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession();

        String userName = (String) httpSession.getAttribute("UserName");
        if(userName == null){
            httpResponse.sendRedirect("login.jsp");
        }else{
            chain.doFilter(httpRequest,httpResponse);
        }
    }
}
```



