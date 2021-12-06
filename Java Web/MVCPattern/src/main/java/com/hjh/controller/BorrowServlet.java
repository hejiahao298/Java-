package com.hjh.controller;

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

@WebServlet(name = "borrow",value = "/borrow")
public class BorrowServlet extends HttpServlet {

    private BookService bookService = new BookServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageStr = req.getParameter("page");
        Integer page = Integer.parseInt(pageStr);
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("reader");
        Integer readerId = reader.getId();
        List<Borrow> list = bookService.getBorrowAllById(readerId,page);
        req.setAttribute("list",list);
        req.setAttribute("currentPage",page);
        req.setAttribute("dataPrePage",6);
        req.setAttribute("pages",bookService.getBorrowPagesByReaderId(readerId,page));
        //转发到首页
        req.getRequestDispatcher("borrow.jsp").forward(req,resp);
    }
}
