package com.hjh.repository;

import com.hjh.entity.Admin;

public interface AdminRepository {
    public Admin Login(String username,String password);
}
