package com.hjh.service.impl;

import com.hjh.repository.AdminRepository;
import com.hjh.repository.ReaderRepository;
import com.hjh.repository.impl.AdminRepositoryImpl;
import com.hjh.repository.impl.ReaderRepositoryImpl;
import com.hjh.service.LoginService;

public class LoginServiceImpl implements LoginService {

    private ReaderRepository readerRepository = new ReaderRepositoryImpl();
    private AdminRepository adminRepository = new AdminRepositoryImpl();

    /**
     * 处理登录的业务
     * @param username
     * @param password
     * @return
     */
    @Override
    public Object login(String username, String password,String type) {
        Object object = null;
        switch (type){
            case "reader":
                object = readerRepository.Login(username,password);
                break;
            case "admin":
                object = adminRepository.Login(username,password);
                break;
        }
        return object;
    }
}
