package com.acc.system.service.impl;

import com.acc.entity.system.LoginInfo;
import com.acc.system.mapper.LoginInfoMapper;
import com.acc.system.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Package com.acc.service
 * @ClassName LoginInfoServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 9:43
 * @Version 1.0
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    public void insertLoginInfo(LoginInfo loginInfo) {
        loginInfoMapper.insertLoginInfo(loginInfo);
    }
}
