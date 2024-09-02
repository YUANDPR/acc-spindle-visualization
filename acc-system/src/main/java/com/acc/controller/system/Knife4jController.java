package com.acc.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * knife4j接口
 */
@Api(tags = "knife4j接口")
@Controller
@RequestMapping("/tool/swagger")
public class Knife4jController extends BaseController {

    @ApiOperation("获取接口文档")
    @RequiresPermissions("tool:swagger:view")
    @GetMapping
    public String index() {
        return redirect("/doc.html");
    }
}
