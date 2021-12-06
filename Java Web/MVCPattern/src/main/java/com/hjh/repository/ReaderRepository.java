package com.hjh.repository;

import com.hjh.entity.Reader;

public interface ReaderRepository {
    public Reader Login(String username,String password);
}
