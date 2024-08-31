package com.acc.system.service;

import com.acc.entity.system.User;

/**
 * @Package com.acc.service.impl
 * @ClassName PasswordService
 * @Description
 * @Author YUAND
 * @Date 2024/8/28 11:25
 * @Version 1.0
 */
public interface PasswordService {

    void validate(User user, String password);

    boolean matches(User user, String newPassword);

    void clearLoginRecordCache(String loginName);

    String encryptPassword(String loginName, String password, String salt);
}
