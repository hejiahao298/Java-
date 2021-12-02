# El表达式
- Expreesion Language表达式语言，代替jsp中数据访问时的复杂编码。可以轻松取出jsp内置对象中保存的数据，前提是一定要先setAttribute,EL就当于简化getattribute

- 基本语法和jqurey相似
    
   > `${  变量名  }`
   
- EL对于4种作用域的默认查找顺序
    
    > pageContext > request > session > application
    
- 指定作用域查找
    
    > request: ${requestScope.name}
    
- EL还可以对取出的值做简单的判断
   > && , || , ! , > , < , <= , >= , ==
   <br/>

### EL使用：

```jsp
   rquest.setAttribute("name","tom"); 
   ${ name } //or ${ requestscope.name }
   ${ num1 > num2 }
```
<br/>

# JSTL

- JSP Standard Tag Library JSP标准标签库，JSP为开发者提供的一系列的标签，使用这些标签可以完成一些基础逻辑处理，如：循环，分支等。使得代码更加简介，不在出现JSP脚本穿插情况。

- JSTL的使用
  > 需要使用两个jar包（jstl.jar  standard.jar）<br/>
  > 需要在JSP页面导入JSTL标签库
  
```JSP
<%@ taglib prefix="c" url="http...."
```
<br/>

- JSTL的优点
   > 提供了统一的标签 <br/>
   > 可以用于编写各种动态功能
   <br/>


### 例子
```java
class user{

private int id;
private String name;
private double score;
private address;

//添加set和get方法和toString方法
}

class address{
private string value;  //添加set和get方法及toString
}
```
<br/>

```jsp
<table>
<tr>
        <th>编号</th>
        <th>姓名</th>
        <th>成绩</th>
        <th>地址</th>
</tr>

<c:forEach items="${list}" var="user">
  <tr>
    <td>${user.id}</td>
    <td>${user.name}</td>
    <td>${user.score}</td>
    <td>${user.address.value}</td>
  </tr>
</c:forEach>
</table>
```
<br/>

## 核心标签库常用标签

 - set , out , remove , catch

<br/>
1. **set: 向域对象中添加数据**

```jsp
<%
  requset.setAttribute(key,value)
%>

<c:set var="name" value="tom" scope="request"></c:set>
${requestScope.name}

<%
User user = new User(1,"张三",66.6,new Address(1,"科技路"));
request.setAttribute("user",user);
%>

${user.name}
<c:set target="${user}" property="name" value="李四"></c:set>
${user.name}
```
<br/>

2. **out: 输出域对象中的数据**

```jsp
<c:set var="name" value="tom"></c:set>
<c:out value="${name}" default="未定义"></c:out>
```
<br/>

3. **remove: 删除域对象中的数据**

```jsp
<c:remove var="name" scope="page"></c:remove>
<c:out value="${name}" default="未定义"></c:out>
```
<br/>

4. **catch: 捕获异常**

```jsp
<c:catch var="error">
  <%
  int a = 10/0;
  %>
</c:catch>
${error}
```
<br/>

5. **if choose 条件标签**

```jsp
<c:set var="num1" value="1"></c:set>
<c:set var="num2" value="2"></c:set>
<c:if test="${num1>num2}">ok</c:if>
<c:if test="${num1<num2}">fail</c:if>
<hr/>
<c:choose>
  <c:when test="${num1>num2}">ok</c:when>
  <c:otherwise>fail</c:otherwise>
</c:choose>
```
<br/>

6. **迭代标签：forEach**

```jsp
<c:forEach items="${list}" var="str" begin="2" end="3" step="2" 
varStatus="sta">
  ${sta.count}, ${str}<br/>
</c:forEach>
```
<br/>

7. **格式化标签库常用的标签**

```jsp
<%
request.setAttribute("date",new Date());
%>
<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss">
</fmt:formatDate><br/>
<fmt:formatNumber value="32145.23434" maxIntegerDigits="2" 
maxFractionDigits="3"></fmt:formatNumber>
```
<br/>
8. **函数标签常用的标签**

```jsp
<%
request.setAttribute("info","Java,C");
%>
${fn:contains(info,"Python")}<br/>
${fn:startsWith(info, "Java")}<br/>
${fn:endsWith(info, "C")}<br/>
${fn:indexOf(info, "va")}<br/>
${fn:replace(info, "C","Python")}<br/>
${fn:substring(info, 2, 3)}<br/>
${fn:split(info, ",")[0]}-${fn:split(info, ",")[1]}
```
