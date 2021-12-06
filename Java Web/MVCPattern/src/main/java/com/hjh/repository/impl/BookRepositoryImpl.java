package com.hjh.repository.impl;

import com.hjh.entity.Book;
import com.hjh.entity.Bookcase;
import com.hjh.repository.BookRepository;
import com.hjh.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    /**
     * 找到id从index开始长度为limit的书本
     * @param index  从那个index的id开始
     * @param limit  查找长度为limit的数据
     *select * from book,bookcase where book.bookcaseid = bookcase.id limit ?,?
     * @return
     */
    @Override
    public List<Book> findAllBook(int index,int limit) {
        Connection connection = JDBCTools.getConnection();
        List<Book> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from book,bookcase where book.bookcaseid = bookcase.id limit ?,?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,index);
            preparedStatement.setInt(2,limit);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Bookcase bookcase = new Bookcase(resultSet.getInt(9),resultSet.getString(10));
                Book book = new Book(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getInt(5),
                resultSet.getFloat(6),
                bookcase);
                list.add(book);
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
     * getPages()计算一共有多少本书
     * @return
     */
    @Override
    public int getPages() {
        Connection connection = JDBCTools.getConnection();
        String sql = "select count(*) from book,bookcase where book.bookcaseid = bookcase.id";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
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
}
