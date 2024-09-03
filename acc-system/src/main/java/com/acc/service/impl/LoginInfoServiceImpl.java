package com.acc.service.impl;

import com.acc.core.entity.LoginInfo;
import com.acc.core.text.Convert;
import com.acc.mapper.LoginInfoMapper;
import com.acc.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    @Override
    public void insertLoginInfo(LoginInfo loginInfo) {
        loginInfoMapper.insertLoginInfo(loginInfo);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<LoginInfo> selectLoginInfoList(LoginInfo loginInfo) {
        return loginInfoMapper.selectLoginInfoList(loginInfo);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    @Override
    public int deleteLoginInfoByIds(String ids) {
        return loginInfoMapper.deleteLoginInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginInfo() {
        loginInfoMapper.cleanLoginInfo();
    }
}
