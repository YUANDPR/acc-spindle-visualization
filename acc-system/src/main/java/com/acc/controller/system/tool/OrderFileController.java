package com.acc.controller.system.tool;

import com.acc.controller.system.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 工单文件处理接口
 */
@Controller
@RequestMapping("/tool/order")
@Api(tags = "工单文件处理")
public class OrderFileController extends BaseController {
    private String prefix = "work/order";

    @RequiresPermissions("tool:order:view")
    @GetMapping
    @ApiOperation("获取工单文件上传页面")
    public String index() {
        return prefix + "/upload";
    }
}
