# JDBC

Java DataBase Connectivity 是⼀个独⽴于特定数据库的管理系统，通⽤的 SQL 数据库存取和操作的公共接⼝。

## JDBC 体系结构

> 定义了⼀组标准，为访问不同数据库提供了统⼀的途径
>
> > ⾯向应⽤的 API，供程序员调⽤
> >
> > ⾯向数据库的 API，供⼚商开发数据库的驱动程序

![JDBC原理](https://raw.githubusercontent.com/hejiahao298/Myimg/master/2.png){:height="200px" width="50%"}

### JDBC API

- 提供者：java官方
- 内容：供开发者调用的接口

> java.sql 和 javax.sql
>
> > DriverManager 类 
> >
> > Connection 接⼝ 
> >
> > Statement 接⼝
> >
> >  ResultSet 接⼝
>
> end



**DriverManager**

> 提供者：Java 官⽅  
>
> 作⽤：管理不同的 JDBC 驱动



**JDBC驱动**

> 提供者：数据库⼚商 
>
> 作⽤：负责连接不同的数据库



### JDBC的使用

**步骤**

1. 加载数据库驱动，Java 程序和数据库之间的桥梁。
2. 获取 Connection，Java 程序与数据库的⼀次连接。
3. 创建 Statement 对象，由 Connection 产⽣，执⾏ SQL 语句。
4. 如果需要接收返回值，创建 ResultSet 对象，保存 Statement 执⾏之后所查询到的结果。

```java
public class JDBCTest {
    @Test
    public void JDBCT(){
        try {
            //加载驱动：运行时类
            Class.forName("com.mysql.cj.jdbc.Driver");
            //用户名，密码，和mysql数据url
            String user = "root";
            String password = "qwer1793";
            String url = "jdbc:mysql://localhost:3307/test";
            
            //通过DriverManager，获取到本次Driver驱动连接
            Connection connection = DriverManager.getConnection(url,user,password);
            //sql语句
            String sql = "select * from student";
            //创建 Statement 对象，由Connection产⽣，此对象用来执⾏ SQL 语句。
            Statement statement = connection.createStatement();
            //将执行的语句结果放入resultSet中
            ResultSet resultSet = statement.executeQuery(sql);
            
            //通过迭代取出resultSet中的值
            while(resultSet.next()) {
                Integer Id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Double score = resultSet.getDouble(3);
                String gender = resultSet.getString(4);
                System.out.println(Id + " - " + name + " - " + score + " - " + gender);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("加载运行时类失败");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
    }
}
```



### Sql增删改

> 增删改使用executeUpdate(sql), 该方法返回一个int类型的数，该数表示在表中有几行发生了改变。

```java
String sql = "insert into student(name,score,birthday) values('李
四',78,'2019-01-01')";
String sql = "update student set name = '李四'";
String sql = "delete from student";
Statement statement = connection.createStatement();
int result = statement.executeUpdate(sql);
```





### PreparedStatment

- Statement的子类，提供了SQL占位符的功能
- SQL占位符会将拼接的String字符串看作一个整体，而不是字符串拼接。

在使用Statement进⾏开发有两个问题：

1. 需要平凡的拼接String字符串，出错率高
2. 存在Sql注入的风险

### 防止sql注入的PreparedStatement方法

```java
Connection connection = DriverManager.getConnection(url,user,password);
String username = "lisi";
String mypassword = "000";
String sql = "select * from t_user where username = ? and password = ?";
System.out.println(sql);
PreparedStatement preparedStatement = connection.prepareStatement(sql);
preparedStatement.setString(1,username);
preparedStatement.setString(2,mypassword);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next()){
 System.out.println("登录成功");
}else{
 System.out.println("登录失败");
}
} catch (ClassNotFoundException e) {
 e.printStackTrace();
} catch (SQLException e){
 e.printStackTrace();
}
```

