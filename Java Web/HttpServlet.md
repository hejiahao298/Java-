# HttpServlet
Servlet 的层次结构
Servlet ---》GenericServlet ---〉HttpServlet

HTTP 请求有很多种类型，常⽤的有四种：
GET 读取
POST 保存
PUT 修改
DELETE 删除
- GenericServlet 实现 Servlet 接⼝，同时为它的⼦类屏蔽了不常⽤的⽅法，⼦类只需要重写 service ⽅法即可。
- HttpServlet 继承 GenericServlet，根据请求类型进⾏分发处理，GET 进⼊ doGET ⽅法，POST 进⼊doPOST ⽅法。
- 开发者⾃定义的 Servlet 类只需要继承 HttpServlet 即可，重新 doGET 和 doPOST。

```java
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "httpServletT", value = "/httpServletT")
public class HttpServletT extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("GET");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Post");
    }
}
```

## **JSP**
- JSP 本质上就是⼀个 Servlet，JSP 主要负责与⽤户交互，将最终的界⾯呈现给⽤户，
- HTML+JS+CSS+Java 的混合⽂件。
- 当服务器接收到⼀个后缀是 jsp 的请求时，将该请求交给 JSP 引擎去处理，每⼀个 JSP ⻚⾯第⼀次被访问的时候，JSP 引擎会将它翻译成⼀个 Servlet ⽂件，再由 Web 容器调⽤ Servlet 完成响应。
- 单纯从开发的⻆度看，JSP 就是在 HTML 中嵌⼊ Java 程序。

**具体的嵌⼊⽅式有 3 种：**
1. JSP 脚本，执⾏ Java 逻辑代码   **<% Java代码 %>**
1. JSP 声明：定义 Java ⽅法           **<%! 声明 Java ⽅法 %>**
1. JSP 表达式：把 Java 对象直接输出到 HTML ⻚⾯中      **<%=Java变量 %>**
```java
<%!
 public String test(){
 return "HelloWorld";
}
%>

<%
String str = test();
%>

<%=str%>
```


### **JSP内置对象 9 个**
1. request：表示⼀次请求，HttpServletRequest。
1. response：表示⼀次响应，HttpServletResponse。
1. pageContext：⻚⾯上下⽂，获取⻚⾯信息，PageContext。
1. session：表示⼀次会话，保存⽤户信息，HttpSession。
1. application：表示当前 Web 应⽤，全局对象，保存所有⽤户共享信息，ServletContext。
1. config：当前 JSP 对应的 Servlet 的 ServletConfig 对象，获取当前 Servlet 的信息。
1. out：向浏览器输出数据，JspWriter。
1. page：当前 JSP 对应的 Servlet 对象，Servlet。
1. exception：表示 JSP ⻚⾯发⽣的异常，Exception
常⽤的是 request、response、session、application、pageContext


### **request 常⽤⽅法：**
```java
String getParameter(String key)            //获取客户端传来的参数。
void setAttribute(String key,Object value) //通过键值对的形式保存数据。
Object getAttribute(String key)            //通过 key 取出 value。
RequestDispatcher getRequestDispatcher(String path) //返回⼀个 RequestDispatcher 对象,该对象的 forward ⽅法⽤于请求转发。
String[] getParameterValues()              //获取客户端传来的多个同名参数。
void setCharacterEncoding(String charset)  //指定每个请求的编码。
```


### **response 常⽤⽅法：**
```java
sendRedirect(String path)                  //重定向，⻚⾯之间的跳转。
```


**转发 getRequestDispatcher 和重定向 sendRedirect 的区别：**
- 转发是将同⼀个请求传给下⼀个⻚⾯，重定向是创建⼀个新的请求传给下⼀个⻚⾯，之前的请求结束⽣命周期。
- 转发：同⼀个请求在服务器之间传递，地址栏不变，也叫服务器跳转。
- 重定向：由客户端发送⼀次新的请求来访问跳转后的⽬标资源，地址栏改变，也叫客户端跳转。
- 如果两个⻚⾯之间需要通过 request 来传值，则必须使⽤转发，不能使⽤重定向。
⽤户登录，如果⽤户名和密码正确，则跳转到⾸⻚（转发），并且展示⽤户名，否则重新回到登陆⻚⾯
（重定向）。


### **HTTP 请求状态码**
- 200：正常
- 404：资源找不到
- 400：请求类型不匹配
- 500：Java 程序抛出异常

### **Session**
**⽤户会话**
- 服务器⽆法识别每⼀次 HTTP 请求的出处（不知道来⾃于哪个终端），它只会接受到⼀个请求信号，所以就存在⼀个问题：将⽤户的响应发送给其他⼈，必须有⼀种技术来让服务器知道请求来⾃哪，这就是会话技术。
- 会话：就是客户端和服务器之间发⽣的⼀系列连续的请求和响应的过程，打开浏览器进⾏操作到关闭浏览器的过程。
- 会话状态：指服务器和浏览器在会话过程中产⽣的状态信息，借助于会话状态，服务器能够把属于同⼀次会话的⼀系列请求和响应关联起来。
**实现会话有两种⽅式：**
1. session
1. cookie
属于同⼀次会话的请求都有⼀个相同的标识符，**sessionID**

### **session 常⽤的⽅法：**
```java
String getId()                             //获取 sessionID
void setMaxInactiveInterval(int interval)  //设置 session 的失效时间，单位为秒
int getMaxInactiveInterval()               //获取当前 session 的失效时间
void invalidate()                          //设置 session ⽴即失效
void setAttribute(String key,Object value) //通过键值对的形式来存储数据
Object getAttribute(String key)            //通过键获取对应的数据
void removeAttribute(String key)           //通过键删除对应的数据
```


### **Cookie**
Cookie 是服务端在 HTTP 响应中附带传给浏览器的⼀个⼩⽂本⽂件，⼀旦浏览器保存了某个 Cookie，
在之后的请求和响应过程中，会将此 Cookie 来回传递，这样就可以通过 Cookie 这个载体完成客户端
和服务端的数据交互。
- 创建 Cookie
```java
Cookie cookie = new Cookie("name","tom");
response.addCookie(cookie);
```
- 读取 Cookie
```java
Cookie[] cookies = request.getCookies();
for (Cookie cookie:cookies){
 out.write(cookie.getName()+":"+cookie.getValue()+"<br/>");
}
```

**Cookie 常⽤的⽅法**
```java
void setMaxAge(int age)  //设置 Cookie 的有效时间，单位为秒
int getMaxAge()          //获取 Cookie 的有效时间
String getName()         //获取 Cookie 的 name
String getValue()        //获取 Cookie 的 value
```

**Session 和 Cookie 的区别**
- session：保存在服务器
- 保存的数据是 Object
- 会随着会话的结束⽽销毁
- 保存重要信息
### **区别**
- cookie：保存在浏览器
- 保存的数据是 String
- 可以⻓期保存在浏览器中，⽆会话⽆关
- 保存不重要信息
**存储⽤户信息：**
- session：setAttribute("name","admin") 存
- getAttribute("name") 取
⽣命周期：服务端：只要 WEB 应⽤重启就销毁，客户端：只要浏览器关闭就销毁。
退出登录：session.invalidate()
cookie：response.addCookie(new Cookie(name,"admin")) 存
```java
Cookie[] cookies = request.getCookies();
for (Cookie cookie:cookies){
 if(cookie.getName().equals("name")){
 out.write("欢迎回来"+cookie.getValue());
 }
}
```
取
⽣命周期：不随服务端的重启⽽销毁，客户端：默认是只要关闭浏览器就销毁，我们通过 setMaxAge()
⽅法设置有效期，⼀旦设置了有效期，则不随浏览器的关闭⽽销毁，⽽是由设置的时间来决定。
退出登录：setMaxAge(0)