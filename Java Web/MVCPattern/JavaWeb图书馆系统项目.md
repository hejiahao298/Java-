# JavaWeb图书馆系统项目

- 运用了Mysql+JDBC+JSP+Maven+MVC模式

### 文件介绍

> mian
>
> > java - 存放后端文件
> >
> > > com.hjh
> > >
> > > > controller - 存放Servlet文件和前端交互，为控制层
> > > >
> > > > repository - 存放repository类和后端交互，为Model层面
> > > >
> > > > entity - 存放实体类，一张表对应一个类，为Model层
> > > >
> > > > service - 存放业务处理类，做为中间层处理两端传来的业务
> > > >
> > > > filter - servlet过滤器
> > > >
> > > > Utils - 存放自己写的工具类(JDBC连接)
> >
> > webapp - 存放前端文件
> >
> > > css ，image , js , jsp
> >
> > end
>
> end



### 主要功能介绍

- 分为读者登录和管理员登录
- 读者申请借阅图书，管理员同意或拒绝
- 读者借书查看页面borrow.jsp
- 管理员归还管理页面return.jsp
- 页面数据的分页功能





