package com.hjh.repository.impl;

import com.hjh.entity.Admin;
import com.hjh.entity.Reader;
import com.hjh.repository.AdminRepository;
import com.hjh.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminRepositoryImpl implements AdminRepository {

    /**
     * 连接数据库，封装读者登录的业务数据方法
     * @param username
     * @param password
     * @return
     */
    public Admin Login(String username, String password) {
        Admin admin = null;
        Connection connection = JDBCTools.getConnection();
        String sql = "select * from bookadmin where username=? and password=?";
        QueryRunner queryRunner = new QueryRunner();
        try {
            admin = queryRunner.query(connection,sql,new BeanHandler<>(Admin.class),username,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return admin;
    }
}
