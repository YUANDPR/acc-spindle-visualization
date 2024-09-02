package com.acc.service;

import com.acc.core.entity.User;

/**
 * @Package com.acc.service
 * @ClassName LoginService
 * @Description
 * @Author YUAND
 * @Date 2024/8/24 17:13
 * @Version 1.0
 */
public interface LoginService {
    User login(String username, String password);
}
