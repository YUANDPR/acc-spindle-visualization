package com.acc.controller.system.monitor;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.LoginInfo;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.LoginInfoService;
import com.acc.service.PasswordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 系统访问记录
 */
@Controller
@RequestMapping("/monitor/loginInfo")
public class LoginInfoController extends BaseController {
    private String prefix = "monitor/loginInfo";

    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private PasswordService passwordService;

    @RequiresPermissions("monitor:loginInfo:view")
    @GetMapping()
    public String loginInfo() {
        return prefix + "/loginInfo";
    }

    @RequiresPermissions("monitor:loginInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LoginInfo loginInfo) {
        startPage();
        List<LoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:loginInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoginInfo loginInfo) {
        List<LoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        ExcelUtil<LoginInfo> util = new ExcelUtil<LoginInfo>(LoginInfo.class);
        return util.exportExcel(list, "登录日志");
    }

    @RequiresPermissions("monitor:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(loginInfoService.deleteLoginInfoByIds(ids));
    }

    @RequiresPermissions("monitor:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        loginInfoService.cleanLoginInfo();
        return success();
    }

    @RequiresPermissions("monitor:loginInfo:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlock(String loginName) {
        passwordService.clearLoginRecordCache(loginName);
        return success();
    }
}
