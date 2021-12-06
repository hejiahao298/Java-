package com.hjh.service.impl;

import com.hjh.entity.Book;
import com.hjh.entity.Borrow;
import com.hjh.repository.BookRepository;
import com.hjh.repository.BorrowRepository;
import com.hjh.repository.impl.BookRepositoryImpl;
import com.hjh.repository.impl.BorrowRepositoryImpl;
import com.hjh.service.BookService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository = new BookRepositoryImpl();
    private BorrowRepository borrowRepository = new BorrowRepositoryImpl();
    private final int LIMIT = 6;

    /**
     * 业务处理：对从servlet传来的page进行业务处理
     * @param page
     * @return
     */
    @Override
    public List<Book> findAllBook(int page) {
        int index = (page-1)*LIMIT;
        return bookRepository.findAllBook(index,LIMIT);
    }

    /**
     * 业务处理：对bookRepository层传来的总书本书进行业务处理，得到index页面总页数。
     * @return
     */
    @Override
    public int getPages() {
        int count = bookRepository.getPages();
        int pages = 0;
        if(count%LIMIT == 0) pages = count/LIMIT;
        else pages = (count/LIMIT)+1;
        return pages;
    }

    /**
     * 业务处理：对从servlet传来的page进行业务处理
     * @param page
     * @param state
     * @return
     */
    @Override
    public List<Borrow> adminFindAll(int page,int state) {
        int index = (page-1)*LIMIT;
        return borrowRepository.adminFindAll(state,index,LIMIT);
    }

    /**
     * 业务处理：计算出借书时间和还书时间，传入borrowRepository，插入到borrow表中
     * @param bookId
     * @param readerId
     */
    @Override
    public void addBorrow(int bookId, int readerId) {
        //借书时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String borrowTime = simpleDateFormat.format(date);
        //还书时间，借书时间+14天
        Calendar calendar = Calendar.getInstance();
        int dates = calendar.get(Calendar.DAY_OF_YEAR) + 14;
        calendar.set(Calendar.DAY_OF_YEAR, dates);
        Date date2 = calendar.getTime();
        String returnTime = simpleDateFormat.format(date2);
        borrowRepository.insert(bookId,readerId,borrowTime,returnTime,null,0);
    }

    /**
     * 业务处理：对BorrowRepository层传来的总申请借阅数进行业务处理，得到admin的页面总页数。
     * @param state
     * @return
     */
    public int getBorrowPagesByState(int state){
        int count = borrowRepository.countByState(state);
        int pages = 0;
        if(count%LIMIT == 0) pages = count/LIMIT;
        else pages = (count/LIMIT)+1;
        return pages;
    }

    /**
     * 业务处理：对从servlet传来的page进行业务处理
     * @param readerId 传入用户的Id查找他所借的书籍
     * @return
     */
    public List<Borrow> getBorrowAllById(int readerId,int page){
        int index = (page-1)*LIMIT;
        return borrowRepository.findAllBook(readerId,index,LIMIT);
    }

    /**
     * 业务处理：对BorrowRepository层传来的总申请借阅数进行业务处理，得到admin的页面总页数。
     * @param readerId 传入用户的Id查找他所借的书籍
     * @return
     */
    public int getBorrowPagesByReaderId(int readerId,int page){
        int count = borrowRepository.getPages(readerId);
        int pages = 0;
        if(count%LIMIT == 0) pages = count/LIMIT;
        else pages = (count/LIMIT)+1;
        return pages;
    }

    /**
     * 业务处理：
     * @param  borrowId 传入借书的Id查找他所借的书籍
     * @return
     */
    public void adminStateChange(int borrowId,int state){
        borrowRepository.adminStateChange(borrowId,state);
    }

    /**
     * 业务处理：对从servlet传来的page进行业务处理
     * @param page
     * @return
     */
    public List<Borrow> adminFindAllReturn(int page){
        int index = (page-1)*LIMIT;
        return borrowRepository.adminFindAllReturn(index,LIMIT);
    }

}
