package com.hjh.repository;

import com.hjh.entity.Borrow;

import java.util.List;

public interface BorrowRepository {
    public List<Borrow> adminFindAll(int state,int index,int limit);

    public void insert(Integer bookid, Integer readerid, String borrowtime, String returntime, Integer adminid, Integer state);

    public int countByState(int state);

    public List<Borrow> findAllBook(int readerId,int page,int limit);

    public int getPages(int readerId);

    public void adminStateChange(int borrowId,int state);

    public List<Borrow> adminFindAllReturn(int index,int limit);
}
