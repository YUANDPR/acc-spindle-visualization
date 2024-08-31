package com.acc.system.service;

import com.acc.entity.system.User;

/**
 * @Package com.acc.service
 * @ClassName UserService
 * @Description
 * @Author YUAND
 * @Date 2024/8/24 18:16
 * @Version 1.0
 */
public interface UserService {
    User selectUserByLoginName(String username);

    void updateUserInfo(User user);
}
