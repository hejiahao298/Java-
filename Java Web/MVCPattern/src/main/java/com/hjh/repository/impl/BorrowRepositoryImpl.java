package com.hjh.repository.impl;

import com.hjh.entity.Book;
import com.hjh.entity.Borrow;
import com.hjh.entity.Reader;
import com.hjh.repository.BorrowRepository;
import com.hjh.utils.JDBCTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowRepositoryImpl implements BorrowRepository {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    /**
     * 查询所有state为0的申请借阅记录，展示在admin.jsp上
     * @param state 默认为0，同意借阅为1，拒绝为2.有人点击借阅时传来0。
     * @param index 默认为1，在service中的处理index = (page-1)*LIMIT，通过前端传来的page参数改变index
     * @param limit 默认为6，final int LIMIT=6;对数据库查询的结果做分割，每次查询6条数据
     * @return
     */
    @Override
    public List<Borrow> adminFindAll(int state, int index, int limit) {
        Connection connection = JDBCTools.getConnection();
        Borrow borrow = null;
        List<Borrow> list = new ArrayList<>();
        String sql = "select br.id,b.name,b.author,b.publish,br.borrowtime,br.returntime,r.name,r.tel,r.cardid,br.state from borrow br,book b,reader r where state = ? and b.id = br.bookid and r.id = br.readerid limit ?,?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,state);
            preparedStatement.setInt(2,index);
            preparedStatement.setInt(3,limit);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                Book book = new Book(resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                Reader reader = new Reader(resultSet.getString(7),resultSet.getString(8),resultSet.getString(9));
                String borrowTime = resultSet.getString(5);
                String returnTime = resultSet.getString(6);
                int state1 = resultSet.getInt(10);
                borrow = new Borrow(id,book,reader,borrowTime,returnTime,state1);
                list.add(borrow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 当读者申请借阅时，向borrow表中插入一条数据
     * @param bookid
     * @param readerid
     * @param borrowtime
     * @param returntime
     * @param adminid
     * @param state
     */
    @Override
    public void insert(Integer bookid, Integer readerid, String borrowtime, String returntime, Integer adminid, Integer state) {
        Connection connection = JDBCTools.getConnection();
        String sql = "insert into borrow(bookid,readerid,borrowtime,returntime,state) values(?,?,?,?,0)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,bookid);
            preparedStatement.setInt(2,readerid);
            preparedStatement.setString(3,borrowtime);
            preparedStatement.setString(4,returntime);
            int per = preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 计算未处理的总申请借阅数，通过BookService的换算，换算成总页数
     * @param state 区分借阅状态
     * @return
     */
    public int countByState(int state){
        int count = 0;
        Connection connection = JDBCTools.getConnection();
        String sql = "select count(*) from borrow br,book b,reader r where state = ? and b.id = br.bookid and r.id = br.readerid";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,state);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                count = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 通过readerId查找他所申请借阅的书籍
     * @param readerId
     * @param page
     * @param limit
     * @return
     */
    public List<Borrow> findAllBook(int readerId,int page,int limit){
        Connection connection = JDBCTools.getConnection();
        Borrow borrow = null;
        List<Borrow> list = new ArrayList<>();
        String sql = "select br.id,b.name,b.author,b.publish,br.borrowtime,br.returntime,r.name,r.tel,r.cardid,br.state from borrow br,book b,reader r where readerid = ? and b.id = br.bookid and r.id = br.readerid limit ?,?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,readerId);
            preparedStatement.setInt(2,page);
            preparedStatement.setInt(3,limit);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                Book book = new Book(resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                Reader reader = new Reader(resultSet.getString(7),resultSet.getString(8),resultSet.getString(9));
                String borrowTime = resultSet.getString(5);
                String returnTime = resultSet.getString(6);
                int state1 = resultSet.getInt(10);
                borrow = new Borrow(id,book,reader,borrowTime,returnTime,state1);
                list.add(borrow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return list;
    }

    public int getPages(int readerId){
        int count = 0;
        Connection connection = JDBCTools.getConnection();
        String sql = "select count(*) from borrow br,book b,reader r where readerid = ? and b.id = br.bookid and r.id = br.readerid";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,readerId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                count = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 通过borrowId当admin同意归还书籍时，state改为3
     * @param borrowId
     * @param state
     */
    public void adminStateChange(int borrowId,int state){
        Connection connection = JDBCTools.getConnection();
        String sql = "update borrow set state=? where id=?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,state);
            preparedStatement.setInt(2,borrowId);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                JDBCTools.release(connection,preparedStatement,null);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    /**
     * 查找所有state为1的书籍（申请通过的书籍）
     * @param index
     * @param limit
     * @return
     */
    public List<Borrow> adminFindAllReturn(int index,int limit){
        Connection connection = JDBCTools.getConnection();
        Borrow borrow = null;
        List<Borrow> list = new ArrayList<>();
        String sql = "select br.id,b.name,b.author,b.publish,br.borrowtime,br.returntime,r.name,r.tel,r.cardid,br.state from borrow br,book b,reader r where state = 1 and b.id = br.bookid and r.id = br.readerid limit ?,?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,index);
            preparedStatement.setInt(2,limit);
           resultSet = preparedStatement.executeQuery();
           while (resultSet.next()){
               int id = resultSet.getInt(1);
               Book book = new Book(resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
               Reader reader = new Reader(resultSet.getString(7),resultSet.getString(8),resultSet.getString(9));
               String borrowTime = resultSet.getString(5);
               String returnTime = resultSet.getString(6);
               int state1 = resultSet.getInt(10);
               borrow = new Borrow(id,book,reader,borrowTime,returnTime,state1);
               list.add(borrow);
           }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                JDBCTools.release(connection,preparedStatement,resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return list;
    }
}
