package com.hjh.controller;

import com.hjh.entity.Book;
import com.hjh.entity.Borrow;
import com.hjh.service.BookService;
import com.hjh.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "admin",value = "/admin")
public class AdminServlet extends HttpServlet {

    private BookService bookService = new BookServiceImpl();

    /**
     * 处理展示在管理员申请借阅列表的逻辑
     * findAll 为默认查找所有用户申请借书的操作（state=0）
     * handle 为同意或者拒绝用户的借书申请req.getParameter("state");
     * getBorrowed 转到还书管理页面
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = null;
        method = req.getParameter("method");
        if(method == null) method = "findAll";
        switch (method){
            case "findAll":
                String pageStr = req.getParameter("page");
                Integer page = Integer.parseInt(pageStr);
                Integer state = 0;
                List<Borrow> list = bookService.adminFindAll(page,state);
                req.setAttribute("list",list);
                req.setAttribute("currentPage",page);
                req.setAttribute("dataPrePage",6);
                req.setAttribute("pages",bookService.getBorrowPagesByState(state));
                //转发到首页
                req.getRequestDispatcher("admin.jsp").forward(req,resp);
                break;
            case "handle":
                String borrowId = req.getParameter("id");
                Integer borrowid = Integer.parseInt(borrowId);
                String stateB = req.getParameter("state");
                Integer stateb = Integer.parseInt(stateB);
                bookService.adminStateChange(borrowid,stateb);
                resp.sendRedirect("/admin?page=1");
                break;
            case "getBorrowed":
                pageStr = req.getParameter("page");
                page = Integer.parseInt(pageStr);
                list = bookService.adminFindAllReturn(page);
                req.setAttribute("list",list);
                req.setAttribute("currentPage",page);
                req.setAttribute("dataPrePage",6);
                req.setAttribute("pages",bookService.getBorrowPagesByState(page));
                req.getRequestDispatcher("return.jsp").forward(req,resp);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
