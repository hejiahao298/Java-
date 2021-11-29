# Ajax异步请求

- Asynchronous JavaScript And XML: 异步的Javascript和XML

- Ajax是一个基于JQuery的前后端交互方式，异步加载，在前后端数据交互时不需要刷新整个页面，只需要刷新修改的部分即可。

### Ajax原理

![](https://raw.githubusercontent.com/hejiahao298/Myimg/master/1.png)



### 基于JQuery的Ajax语法

```jsp
$.ajax({属性}){
  常⽤的属性参数：
  url：请求的后端服务地址
  type：请求⽅式，默认 get
  data：请求参数
  dataType：服务器返回的数据类型，text/json
  success：请求成功的回调函数
  error：请求失败的回调函数
  complete：请求完成的回调函数（⽆论成功或者失败，都会调⽤）
}
```

> JSON
>
> > JavaScript Object Notation，⼀种轻量级数据交互格式，完成 js 与 Java 等后端开发语⾔对象数据之间 的转换
> >
> > 客户端和服务器之间传递对象数据，需要⽤ JSON 格式。
> >
> > 需要导入JSON.jar包



## 实例

> 通过Ajax异步的方式完成: 省份 / 城市 / 城区 的三相联动

####  JAVAServlet代码

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "location", value = "/location")
public class LocationServlet extends HttpServlet {
    private static Map<String, List<String>> provinceMap;
    private static Map<String, List<String>> cityMap;

    static {
        //充当数据库
        /* -------------------省份-城市begin---------------- */
        provinceMap = new HashMap<>();
        List<String> cities = new ArrayList<>();
        cities.add("请选择");
        cities.add("西安市");
        cities.add("宝鸡市");
        cities.add("渭南市");
        provinceMap.put("陕西省",cities);

        cities = new ArrayList<>();
        cities.add("请选择");
        cities.add("郑州市");
        cities.add("洛阳市");
        cities.add("开封市");
        provinceMap.put("河南省",cities);

        cities = new ArrayList<>();
        cities.add("请选择");
        cities.add("南京市");
        cities.add("苏州市");
        cities.add("南通市");
        provinceMap.put("江苏省",cities);
        /* -------------------省份-城市end---------------- */


        /* -------------------城市-地区begin---------------- */
        //陕西省
        cityMap = new HashMap<>();
        List<String> areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("雁塔区");
        areas.add("莲湖区");
        areas.add("新城区");
        cityMap.put("西安市",areas);

        areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("陈仓区");
        areas.add("渭滨区");
        areas.add("新城区");
        cityMap.put("宝鸡市",areas);

        areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("临渭区");
        areas.add("高新区");
        cityMap.put("渭南市",areas);

        //河南省
        areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("郑州A区");
        areas.add("郑州B区");
        cityMap.put("郑州市",areas);

        areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("洛阳A区");
        areas.add("洛阳B区");
        cityMap.put("洛阳市",areas);

        areas = new ArrayList<>();
        cities.add("请选择");
        areas.add("开封A区");
        areas.add("开封B区");
        cityMap.put("开封市",areas);

        //江苏省
        //.....
        /* -------------------城市-地区end---------------- */
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        //获取id和type参数
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        
        JSONArray jsonArray = null;
        //通过type的类型进行处理
        switch (type){
            case "city":
                List<String> areas = cityMap.get(id);
                jsonArray = JSONArray.fromObject(areas);
                resp.getWriter().write(jsonArray.toString());
                break;
            case "province":
                List<String> cities = provinceMap.get(id);
                jsonArray = JSONArray.fromObject(cities);
                resp.getWriter().write(jsonArray.toString());
                break;

        }

    }
}
```



#### JSP代码

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script>
        $(function () {
            //修改省份-城市
            $("#province").change(function () {
                var id = $(this).val();    
                $.ajax({
                    url: "/location",
                    type: "post",
                    data: "id=" + id + "&type=province",
                    dataType: "JSON",
                    success: function (data) {
                        var content = "";
                        for (var i = 0; i < data.length; i++) {
                            content += "<option>" + data[i] + "</option>";
                        }
                        $("#city").html(content);
                    }
                });
            });

            //修改城市-地区
            $("#city").change(function () {
                var id = $(this).val();
                $.ajax({
                    url: "/location",
                    type: "post",
                    data: "id=" + id + "&type=city",
                    dataType: "JSON",
                    success: function (data) {
                        var content = "";
                        for (var i = 0; i < data.length; i++) {
                            content += "<option>" + data[i] + "</option>";
                        }
                        $("#area").html(content);
                    }
                });
            });
        });
    </script>
</head>
<body>

    省: <select id="province">
        <option value="null">请选择</option>
        <option value="陕西省">陕西省</option>
        <option value="河南省">河南省</option>
        <option value="江苏省">江苏省</option>
    </select>

    市: <select id="city">
        <option value="null">请选择</option>
    </select>

    区: <select id="area">
        <option>请选择</option>
    </select>

</body>
</html>
```





 











