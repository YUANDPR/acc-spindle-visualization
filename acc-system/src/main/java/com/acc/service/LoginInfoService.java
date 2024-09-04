package com.acc.service;

import com.acc.core.entity.LoginInfo;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 */
public interface LoginInfoService {
    /**
     * 新增系统登录日志
     *
     * @param LoginInfo 访问日志对象
     */
    void insertLoginInfo(LoginInfo LoginInfo);

    /**
     * 查询系统登录日志集合
     *
     * @param LoginInfo 访问日志对象
     * @return 登录记录集合
     */
    List<LoginInfo> selectLoginInfoList(LoginInfo LoginInfo);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteLoginInfoByIds(String ids);

    /**
     * 清空系统登录日志
     */
    void cleanLoginInfo();
}
