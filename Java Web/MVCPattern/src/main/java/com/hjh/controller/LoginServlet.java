package com.hjh.controller;

import com.hjh.entity.Admin;
import com.hjh.entity.Book;
import com.hjh.entity.Reader;
import com.hjh.service.BookService;
import com.hjh.service.LoginService;
import com.hjh.service.impl.BookServiceImpl;
import com.hjh.service.impl.LoginServiceImpl;
import com.mysql.cj.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "login",value = "/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = new LoginServiceImpl();
    private BookService bookService = new BookServiceImpl();

    /**
     * 处理登录的业务逻辑
     * admin 跳到管理员首页
     * reader 跳到用户首页
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reader reader = null;
        Admin admin = null;
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String type = req.getParameter("type");
        Object object = loginService.login(username,password,type);
        if(object != null) {
            switch (type) {
                case "admin":
                    admin = (Admin) object;
                    req.getSession().setAttribute("admin", admin);
                    resp.sendRedirect("/admin?page=1");
                    break;
                case "reader":
                    reader = (Reader) object;
                    req.getSession().setAttribute("reader", reader);
                    List<Book> list = bookService.findAllBook(1);
                    req.setAttribute("list",list);
                    req.setAttribute("currentPage",1);
                    req.setAttribute("dataPrePage",6);
                    req.setAttribute("pages",bookService.getPages());
                    //转发到首页
                    req.getRequestDispatcher("index.jsp").forward(req,resp);
                    break;
            }
        }else{
            resp.sendRedirect("login.jsp");
        }
    }
}
