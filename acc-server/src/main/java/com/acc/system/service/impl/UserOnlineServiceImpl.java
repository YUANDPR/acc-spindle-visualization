package com.acc.system.service.impl;

import com.acc.constant.ShiroConstants;
import com.acc.entity.system.UserOnline;
import com.acc.system.mapper.UserOnlineMapper;
import com.acc.system.service.UserOnlineService;
import com.acc.utils.DateUtils;
import com.acc.utils.SpringUtils;
import com.acc.utils.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.Deque;
import java.util.List;

/**
 * @Package com.acc.service.impl
 * @ClassName UserOnlineServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 10:59
 * @Version 1.0
 */
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

    @Autowired
    private UserOnlineMapper userOnlineMapper;

    /**
     * 清理用户缓存
     *
     * @param loginName 登录名称
     * @param sessionId 会话ID
     */
    @Override
    public void removeUserCache(String loginName, String sessionId) {
        EhCacheManager ehCacheManager = SpringUtils.getBean(EhCacheManager.class);
        Cache<String, Deque<Serializable>> cache = ehCacheManager.getCache(ShiroConstants.SYS_USERCACHE);
        Deque<Serializable> deque = cache.get(loginName);
        if (StringUtils.isEmpty(deque) || deque.isEmpty()) {
            return;
        }
        deque.remove(sessionId);
    }

    @Override
    public UserOnline selectOnlineById(String sessionId) {
        return userOnlineMapper.selectOnlineById(sessionId);
    }

    /**
     * 查询会话集合
     *
     * @param expiredDate 失效日期
     */
    @Override
    public List<UserOnline> selectOnlineByExpired(Date expiredDate) {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        return userOnlineMapper.selectOnlineByExpired(lastAccessTime);
    }

    /**
     * 通过会话序号删除信息
     *
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    @Override
    public void batchDeleteOnline(List<String> sessions) {
        for (String sessionId : sessions) {
            UserOnline userOnline = selectOnlineById(sessionId);
            if (StringUtils.isNotNull(userOnline)) {
                userOnlineMapper.deleteOnlineById(sessionId);
            }
        }
    }

    /**
     * 保存会话信息
     *
     * @param online 会话信息
     */
    @Override
    public void saveOnline(UserOnline online) {
        userOnlineMapper.saveOnline(online);
    }

    /**
     * 通过会话序号删除信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public void deleteOnlineById(String sessionId) {
        UserOnline userOnline = selectOnlineById(sessionId);
        if (StringUtils.isNotNull(userOnline)) {
            userOnlineMapper.deleteOnlineById(sessionId);
        }
    }
}
