# JSP实例
运用了EL+JSTL+Servlet

### User class
```java
public class User {
    private int id;
    private String name;
    private double Score;

    public User(int id, String name, double score) {
        this.id = id;
        this.name = name;
        Score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Score=" + Score +
                '}';
    }
}
```


### Servlet服务项
```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {

    //设置静态全局变量map（HashMap）
    private static Map<Integer, User> map = new HashMap<>();
    static {
        /*
         * 向map添加数据充当数据库
         * */
        map.put(1, new User(1, "张三", 75.8));
        map.put(2, new User(2, "李四", 90.5));
        map.put(3, new User(3, "王五", 45.0));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        /*
        * 从EL_JSTLIndex.jsp中传过来的method判断所要执行的操作。
        * findall为默认操作。
        * delete删除 id为jsp传来的参数，删除指定对象。
        * findById修改数据操作，将传来的id请求转发到update.jsp。
        * */
        String method = req.getParameter("method");
        if (method == null) method = "findall";
        switch (method) {
            case "findall":
                req.setAttribute("map", map.values());
                req.getRequestDispatcher("EL_JSTLIndex.jsp").forward(req, resp);
                break;
            case "delete":
                String idstr = req.getParameter("id");
                Integer id = Integer.parseInt(idstr);
                map.remove(id);
                resp.sendRedirect("/UserServlet");
                break;
            case "findById":
                idstr = req.getParameter("id");
                id = Integer.parseInt(idstr);
                req.setAttribute("User", map.get(id));
                req.getRequestDispatcher("update.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        /*
        * 处理来自update.jsp页面和Eladd.jsp页面的请求。
        * update修改数据。
        * Eladd添加用户。
        * */
        String idstr = req.getParameter("id");
        String name = req.getParameter("name");
        String scorestr = req.getParameter("score");
        Integer id = Integer.parseInt(idstr);
        Double score = Double.parseDouble(scorestr);
        map.put(id, new User(id, name, score));
        resp.sendRedirect("/UserServlet");
    }
}
```

### 
### **JSP_index**
```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table>
    <tr>
        <th>编号</th>
        <th>姓名</th>
        <th>成绩</th>
        <th>操作</th>
    </tr>

    <c:forEach items="${map}" var="User">
       <tr>
           <td>
               ${User.id}
           </td>
           <td>
                ${User.name}
           </td>
           <td>
                ${User.score}

           </td>
           <td>
               <a href="/UserServlet?method=delete&id=${User.id}">删除</a>
               <a href="/UserServlet?method=findById&id=${User.id}">修改</a>
           </td>
       </tr>
    </c:forEach>

</table>
</body>
</html>
```

### **JSP_update**
```java
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/UserServlet" method="post">
    编号：<input type="text" name="id" value="${User.id}" readonly>
    姓名：<input type="text" name="name" value="${User.name}">
    成绩：<input type="text" name="score" value="${User.score}">
    <input type="submit" value="修改">
</form>
</body>
</html>
```

### **JSP_addUser**
```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/UserServlet" method="post">
    编号：<input type="text" name="id">
    姓名：<input type="text" name="name">
    成绩：<input type="text" name="score">
    <input type="submit" value="添加">
</form>
</body>
</html>
```
