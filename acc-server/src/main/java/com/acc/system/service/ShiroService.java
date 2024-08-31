package com.acc.system.service;

import com.acc.system.session.OnlineSession;
import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * @Package com.acc.service
 * @ClassName ShiroService
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 14:56
 * @Version 1.0
 */
public interface ShiroService {
    Session getSession(Serializable sessionId);

    void deleteSession(OnlineSession onlineSession);
}
