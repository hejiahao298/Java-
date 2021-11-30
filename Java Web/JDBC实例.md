# JDBC实例

> 运用了JSP+JDBC+Servlet

## JSP

- index.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<table>
    <tr>
        <th>编号</th>
        <th>性别</th>
        <th>分数</th>
        <th>性别</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${list}" var="student">
        <tr>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.score}</td>
            <td>${student.gender}</td>
            <td><a href="/student?type=delete&id=${student.id}">删除</a>
                <a href="/student?type=findById&id=${student.id}">修改</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
```



- add.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="/student?type=add">
    姓名: <input type="text" name="name"><br/>
    分数: <input type="text" name="score"><br/>
    性别: <input type="text" name="gender"><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
```



- update.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="/student?type=update">
    编号: <input type="text" name="id" value="${id}" readonly>
    姓名: <input type="text" name="name" value="${name}"><br/>
    分数: <input type="text" name="score" value="${score}"><br/>
    性别: <input type="text" name="gender" value="${gender}"><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
```



## Student

```java
public class Student {

    public Student(int id, String name, double score, String gender) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.gender = gender;
    }

    private int id;
    private String name;
    private double score;
    private String gender;

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
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", gender='" + gender + '\'' +
                '}';
    }
}
```





## Repository.java

```java
/*
* 该类面向数据库，封装操作数据库的方法，供给servlet使用
* */
public class StudentPer {
    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    //查询所有数据的方法
    public List<Student> FindAll(){
        List<Student> list = new ArrayList<>();
        String sql = "select * from student";
        try {
            Connection connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Double score = resultSet.getDouble(3);
                String gender = resultSet.getString(4);
                list.add(new Student(id,name,score,gender));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.release(connection,statement,resultSet);
        }
        return list;
    }

    //添加数据
    public void add(String name,double score,String gender){
        String sql = "insert into student(name,score,gender) values (?,?,?)";
        try {
            Connection connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setDouble(2,score);
            preparedStatement.setString(3,gender);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            JDBCUtil.release(connection,preparedStatement,null);
        }

    }


    //删除数据
    public void delete(int id){
        String sql = "delete from student where id=?";
        try {
            Connection connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.release(connection,preparedStatement,null);
        }

    }

    //通过id查找数据
    public Student findById(int id){
        String sql = "select * from student where id=?";
        Student student = null;
        try {
            Connection connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer id2 = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Double score = resultSet.getDouble(3);
                String gender = resultSet.getString(4);
                student = new Student(id2,name,score,gender);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtil.release(connection,preparedStatement,resultSet);
        }
        return student;
    }

    //更改数据
    public void update(int id,String name,double score,String gender){
        String sql = "update student set name=?,score=?,gender=? where id = ?";
        try {
            Connection connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);;
            preparedStatement.setString(1,name);
            preparedStatement.setDouble(2,score);
            preparedStatement.setString(3,gender);
            preparedStatement.setInt(4,id);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            JDBCUtil.release(connection,preparedStatement,null);
        }
    }

}
```



## Servlet

```java
@WebServlet(name = "student", value = "/student")
public class StudentServlet extends HttpServlet {
    StudentPer studentPer = new StudentPer();
    @Override

    /*
    * doGet处理index.jsp的请求
    * 主要功能查询和删除
    * */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String idStr = "";
        Integer id = null;

        if(type == null){
            type = "findAll";
        }
        switch (type){
            case "findAll":
                List<Student> list = studentPer.FindAll();
                req.setAttribute("list",list);
                req.getRequestDispatcher("index.jsp").forward(req,resp);
                break;
            case "delete":
                idStr = req.getParameter("id");
                id = Integer.parseInt(idStr);
                studentPer.delete(id);
                resp.sendRedirect("/student");
                break;
            case "findById":
                idStr = req.getParameter("id");
                id = Integer.parseInt(idStr);
                Student student = studentPer.findById(id);
                req.setAttribute("id",student.getId());
                req.setAttribute("name",student.getName());
                req.setAttribute("score",student.getScore());
                req.setAttribute("gender",student.getGender());
                req.getRequestDispatcher("update.jsp").forward(req,resp);
                break;
        }

    }

    /*
    * doPost处理来自add.jsp和update.jsp的请求，
    * 主要功能添加和修改
    * */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");

        String name = req.getParameter("name");
        String scoreStr = req.getParameter("score");
        Double score = Double.parseDouble(scoreStr);
        String gender = req.getParameter("gender");
        switch (type){
            case "add":
                studentPer.add(name,score,gender);
                break;
            case "update":
                String idStr = req.getParameter("id");
                Integer id = Integer.parseInt(idStr);
                studentPer.update(id,name,score,gender);
                break;
        }
        resp.sendRedirect("/student");
        }

}
```



## Filter

```java
@WebFilter(filterName = "FilterDemo01", urlPatterns = { "/student" })
public class CharacterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
```













