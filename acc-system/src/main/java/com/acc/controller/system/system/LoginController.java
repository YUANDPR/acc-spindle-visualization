package com.acc.controller.system.system;

import com.acc.controller.system.BaseController;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ServletUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录接口
 */
@Controller
@Slf4j
@Api(tags = "登录")
public class LoginController extends BaseController {

    /**
     * 是否开启记住我功能
     */
    @Value("${shiro.rememberMe.enabled}")
    private boolean rememberMe;

    @GetMapping("/login")
    @ApiOperation("管理端获取登录页面")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap) {
        log.info("接收到登录请求");

        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        // 是否开启记住我
        mmap.put("isRemembered", rememberMe);
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("管理端登录")
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
        log.info("开始登录，用户名：{}", username);

        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return AjaxResult.success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    @GetMapping("/unauth")
    @ApiOperation("无权限跳转页面")
    public String unauth() {
        return "error/unauth";
    }

}

