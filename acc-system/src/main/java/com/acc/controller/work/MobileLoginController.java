package com.acc.controller.work;

import com.acc.core.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/android")
@Api(tags = "移动端登录")
public class MobileLoginController {

    @GetMapping("/login")
    @ResponseBody
    @ApiOperation("移动端登录")
    @Anonymous
    public ResponseEntity<?> getWorkOrder(@RequestParam String username, @RequestParam String password) {
        System.out.println("1111");
        System.out.println("username : " + username);
        System.out.println("password : " + password);
        if (username.equals("admin") && password.equals("admin")) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else return new ResponseEntity<>("fail", HttpStatus.FORBIDDEN);
    }
}
