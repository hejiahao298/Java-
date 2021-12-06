package com.hjh.service;

import com.hjh.entity.Book;
import com.hjh.entity.Borrow;

import java.util.List;

public interface BookService {
    /**
     * 业务处理：对从servlet传来的page进行业务处理
     * @param page
     * @return
     */
    public List<Book> findAllBook(int page);

    /**
     * 业务处理：对bookRepository层传来的总书本书进行业务处理，得到index页面总页数。
     * @return
     */
    public int getPages();

    public List<Borrow> adminFindAll(int page,int state);

    public void addBorrow(int bookId,int readerId);

    public int getBorrowPagesByState(int state);

    public List<Borrow> getBorrowAllById(int readerId,int page);

    public int getBorrowPagesByReaderId(int readerId,int page);

    public void adminStateChange(int borrowId,int state);

    public List<Borrow> adminFindAllReturn(int page);
}
