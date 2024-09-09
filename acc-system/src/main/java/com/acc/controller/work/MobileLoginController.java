package com.acc.controller.work;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端登录接口
 */
@Controller
@Slf4j
@RequestMapping("/android")
@Api(tags = "移动端登录")
public class MobileLoginController {

    @GetMapping("/login")
    @ResponseBody
    @ApiOperation("移动端登录")
    public ResponseEntity<?> getWorkOrder(@RequestParam String username, @RequestParam String password) {
        log.info("移动端开始登录，用户名：{}", username);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }
    }
}
