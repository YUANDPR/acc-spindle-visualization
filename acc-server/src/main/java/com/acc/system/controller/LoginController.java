package com.acc.system.controller;

import com.acc.result.AjaxResult;
import com.acc.system.utils.ServletUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录接口
 */
@Controller
@Slf4j
@Api(tags = "登录接口")
public class LoginController extends BaseController {

    /**
     * 是否开启记住我功能
     */
    @Value("${shiro.rememberMe.enabled}")
    private boolean rememberMe;

    @ApiOperation("管理端获取登录页面")
    @GetMapping("/login")
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

    @ApiOperation("手机端登录")
    @GetMapping("/android/login")
    public ResponseEntity<?> getWorkOrder(@RequestParam String username, @RequestParam String password) {
        System.out.println("1111");
        System.out.println("username : " + username);
        System.out.println("password : " + password);
        if (username.equals("admin") && password.equals("admin")) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else return new ResponseEntity<>("fail", HttpStatus.FORBIDDEN);
    }

    @ApiOperation("管理端登录")
    @PostMapping("/login")
    @ResponseBody
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

    @ApiOperation("无权限跳转页面")
    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }

}

