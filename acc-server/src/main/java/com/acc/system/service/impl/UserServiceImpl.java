package com.acc.system.service.impl;

import com.acc.entity.system.User;
import com.acc.system.mapper.UserMapper;
import com.acc.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Package com.acc.service.impl
 * @ClassName UserServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 9:39
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserByLoginName(String userName) {
        return userMapper.selectUserByLoginName(userName);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateUser(user);
    }
}
