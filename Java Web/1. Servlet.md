# **Servlet**
- 什么是 Servlet？
Servlet 是 Java Web 开发的基⽯，与平台⽆关的服务器组件，它是运⾏在 Servlet 容器/Web 应⽤服务
器/Tomcat，负责与客户端进⾏通信。
- Servlet 的功能：
1、创建并返回基于客户请求的动态 HTML ⻚⾯。
2、与数据库进⾏通信。
- 如何使⽤ Servlet？
Servlet 本身是⼀组接⼝，⾃定义⼀个类，并且实现 Servlet 接⼝，这个类就具备了接受客户端请求以及
做出响应的功能

Servlet 的⽣命周期⽅法：⽆参构造函数、init、service、destory
1、⽆参构造函数只调⽤⼀次，创建对象。
2、init 只调⽤⼀次，初始化对象。
3、service 调⽤ N 次，执⾏业务⽅法。
4、destory 只调⽤⼀次，卸载对象。
```java
import javax.naming.Context;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

public class MyServlet implements Servlet {
    @Override
    
    //Servlet对象初始化
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("MyServlet初始化");
        System.out.println(servletConfig.getServletName());               //返回 Servlet 的名称，全类名
        System.out.println(servletConfig.getInitParameter("password"));   //获取 init 参数的值（web.xml）
        
        //getServletContext()返回ServletContext 对象，它是 Servlet 的上下⽂，整个 Servlet 的管理者。
        ServletContext context = servletConfig.getServletContext();
        System.out.println(context.getInitParameter("context"));   //获取该项目环境参数
        System.out.println(context.getContextPath());     //返回该项目路径
    }

    
    public ServletConfig getServletConfig() {
        return null;
    }

   //最主要的方法，主要服务方法
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("MyServlet正在服务");
        res.setContentType("text/html;charset=UTF-8");
        res.getWriter().write("后台正在进行");
    }


    public String getServletInfo() {
        return null;
    }

    //当关闭服务起或重启服务器，Servlet对象自动执行该方法
    public void destroy() {
        System.out.println("MyServlet资源被释放");
    }
}
```

ServletConfig 和 ServletContext 的区别：
ServletConfig 作⽤于某个 Servlet 实例，每个 Servlet 都有对应的 ServletConfig，ServletContext 作⽤
于整个 Web 应⽤，⼀个 Web 应⽤对应⼀个 ServletContext，多个 Servlet 实例对应⼀个
ServletContext。
⼀个是局部对象，⼀个是全局对象。


浏览器不能直接访问 Servlet ⽂件，只能通过映射的⽅式来间接访问 Servlet，映射需要开发者⼿动配
置，有两种配置⽅式。
- 基于 XML ⽂件的配置⽅式。
```html
<servlet>
 <servlet-name>hello</servlet-name>
 <servlet-class>com.southwind.servlet.HelloServlet</servlet-class>
</servlet>
<servlet-mapping>
 <servlet-name>hello</servlet-name>
 <url-pattern>/demo2</url-pattern>
</servlet-mapping>
```

- 基于注解的方式
```java
@WebServlet("/demo2")
public class HelloServlet implements Servlet {
 }
```
上述两种配置⽅式结果完全⼀致，将 demo2 与 HelloServlet 进⾏映射，即在浏览器地址栏中直接访问
demo 就可以映射到 HelloServlet。