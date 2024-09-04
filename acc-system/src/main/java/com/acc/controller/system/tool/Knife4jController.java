package com.acc.controller.system.tool;

import com.acc.controller.system.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * knife4j接口
 */
@Controller
@RequestMapping("/tool/swagger")
@Api(tags = "knife4j")
public class Knife4jController extends BaseController {

    @RequiresPermissions("tool:swagger:view")
    @GetMapping
    @ApiOperation("获取接口文档页面")
    public String index() {
        return redirect("/doc.html");
    }
}
