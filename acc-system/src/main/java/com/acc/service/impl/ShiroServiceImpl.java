package com.acc.service.impl;

import com.acc.core.entity.UserOnline;
import com.acc.core.session.OnlineSession;
import com.acc.core.utils.StringUtils;
import com.acc.service.ShiroService;
import com.acc.service.UserOnlineService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Package com.acc.service.impl
 * @ClassName ShiroServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/26 14:56
 * @Version 1.0
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private UserOnlineService onlineService;

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    @Override
    public Session getSession(Serializable sessionId) {
        UserOnline userOnline = onlineService.selectOnlineById(String.valueOf(sessionId));
        return StringUtils.isNull(userOnline) ? null : createSession(userOnline);
    }

    /**
     * 删除会话
     *
     * @param onlineSession 会话信息
     */
    public void deleteSession(OnlineSession onlineSession) {
        onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
    }

    public Session createSession(UserOnline userOnline) {
        OnlineSession onlineSession = new OnlineSession();
        if (StringUtils.isNotNull(userOnline)) {
            onlineSession.setId(userOnline.getSessionId());
            onlineSession.setHost(userOnline.getIpaddr());
            onlineSession.setBrowser(userOnline.getBrowser());
            onlineSession.setOs(userOnline.getOs());
            onlineSession.setDeptName(userOnline.getDeptName());
            onlineSession.setLoginName(userOnline.getLoginName());
            onlineSession.setStartTimestamp(userOnline.getStartTimestamp());
            onlineSession.setLastAccessTime(userOnline.getLastAccessTime());
            onlineSession.setTimeout(userOnline.getExpireTime());
        }
        return onlineSession;
    }
}
