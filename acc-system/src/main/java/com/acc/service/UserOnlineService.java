package com.acc.service;

import com.acc.core.entity.UserOnline;

import java.util.Date;
import java.util.List;

/**
 * @Package com.acc.service
 * @ClassName UserOnlineService
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 10:59
 * @Version 1.0
 */
public interface UserOnlineService {
    void removeUserCache(String loginName, String sessionId);

    List<UserOnline> selectOnlineByExpired(Date expiredDate);

    UserOnline selectOnlineById(String sessionId);

    void batchDeleteOnline(List<String> needOfflineIdList);

    void saveOnline(UserOnline online);

    void deleteOnlineById(String sessionId);
}
