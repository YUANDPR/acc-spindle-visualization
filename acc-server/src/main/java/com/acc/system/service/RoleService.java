package com.acc.system.service;

import java.util.Set;

/**
 * @Package com.acc.service
 * @ClassName RoleService
 * @Description
 * @Author YUAND
 * @Date 2024/8/24 17:08
 * @Version 1.0
 */
public interface RoleService {
    Set<String> selectRoleKeys(Long userId);
}
