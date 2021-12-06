package com.hjh.controller;

import com.hjh.entity.Book;
import com.hjh.entity.Borrow;
import com.hjh.entity.Reader;
import com.hjh.service.BookService;
import com.hjh.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "book",value = "/book")
public class BookServlet extends HttpServlet {

    private BookService bookService = new BookServiceImpl();

    /**
     * 处理展示在首页书列表的逻辑
     * 通过findAllBook(page)通过page参数控制显示首页的数据
     * findAll 默认，查找所有的书籍。
     * addBorrow 添加申请借阅请求。
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       String method = req.getParameter("method");
       HttpSession session = req.getSession();
       Reader reader = (Reader) session.getAttribute("reader");
        String pageStr = null;
        Integer page = null;
        pageStr = req.getParameter("page");
       if(method == null) method="findAll";
        switch (method){
            case "findAll":

                page = Integer.parseInt(pageStr);
                List<Book> list = bookService.findAllBook(page);
                req.setAttribute("list",list);
                req.setAttribute("currentPage",page);
                req.setAttribute("dataPrePage",6);
                req.setAttribute("pages",bookService.getPages());
                //转发到首页
                req.getRequestDispatcher("index.jsp").forward(req,resp);
                break;
            case "addBorrow":
                String bookId = req.getParameter("bookid");
                Integer bookid = Integer.parseInt(bookId);
                bookService.addBorrow(bookid,reader.getId());
                resp.sendRedirect("/borrow?page=1");
                break;
        }

    }
}
