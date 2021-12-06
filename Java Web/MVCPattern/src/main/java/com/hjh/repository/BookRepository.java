package com.hjh.repository;

import com.hjh.entity.Book;

import java.util.List;

public interface BookRepository {

    /**
     * 找到id从index开始长度为limit的书本
     * @param index  从那个index的id开始
     * @param limit  查找长度为limit的数据
     *select * from book,bookcase where book.bookcaseid = bookcase.id limit ?,?
     * @return
     */
    public List<Book> findAllBook(int index,int limit);

    /**
     * getPages()计算一共有多少本书
     * @return
     */
    public int getPages();
}
