package com.hjh.repository.impl;

import com.hjh.entity.Reader;
import com.hjh.repository.ReaderRepository;
import com.hjh.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class ReaderRepositoryImpl implements ReaderRepository {

    /**
     * 连接数据库，封装读者登录的业务数据方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public Reader Login(String username, String password) {
        Reader reader = null;
        Connection connection = JDBCTools.getConnection();
        String sql = "select * from reader where username=? and password=?";
        QueryRunner queryRunner = new QueryRunner();
        try {
            reader = queryRunner.query(connection,sql,new BeanHandler<>(Reader.class),username,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reader;
    }
}
