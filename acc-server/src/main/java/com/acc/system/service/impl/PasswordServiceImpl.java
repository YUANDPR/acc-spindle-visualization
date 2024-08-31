package com.acc.system.service.impl;

import com.acc.constant.Constants;
import com.acc.constant.ShiroConstants;
import com.acc.entity.system.User;
import com.acc.system.exception.user.UserPasswordNotMatchException;
import com.acc.system.exception.user.UserPasswordRetryLimitExceedException;
import com.acc.system.manager.AsyncManager;
import com.acc.system.manager.factory.AsyncFactory;
import com.acc.system.service.PasswordService;
import com.acc.utils.MessageUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Package com.acc.service.impl
 * @ClassName PasswordServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 9:41
 * @Version 1.0
 */
@Service
public class PasswordServiceImpl implements PasswordService {


    @Autowired
    private CacheManager cacheManager;

    private Cache<String, AtomicInteger> loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    @PostConstruct
    public void init() {
        loginRecordCache = cacheManager.getCache(ShiroConstants.LOGIN_RECORD_CACHE);
    }

    /**
     * 用户验证
     *
     * @param user     用户
     * @param password 密码
     */
    @Override
    public void validate(User user, String password) {
        String loginName = user.getLoginName();

        AtomicInteger retryCount = loginRecordCache.get(loginName);

        // 记录登陆次数
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            loginRecordCache.put(loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue()) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }
        if (!matches(user, password)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            loginRecordCache.put(loginName, retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(loginName);
        }
    }

    @Override
    public boolean matches(User user, String newPassword) {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    @Override
    public void clearLoginRecordCache(String loginName) {
        loginRecordCache.remove(loginName);
    }

    @Override
    public String encryptPassword(String loginName, String password, String salt) {

        return new Md5Hash(loginName + password + salt).toHex();
    }
}
