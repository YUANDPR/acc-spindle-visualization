package com.acc.enumeration;

import lombok.Getter;

/**
 * 用户会话
 */
@Getter
public enum OnlineStatus {
    /**
     * 用户状态
     */
    on_line("在线"), off_line("离线");

    private final String info;

    OnlineStatus(String info) {
        this.info = info;
    }

}
