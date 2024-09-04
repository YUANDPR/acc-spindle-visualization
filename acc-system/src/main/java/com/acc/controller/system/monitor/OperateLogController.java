package com.acc.controller.system.monitor;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.OperLog;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 */
@Controller
@RequestMapping("/monitor/operlog")
@Api(tags = "操作日志")
public class OperateLogController extends BaseController {
    private String prefix = "monitor/operlog";

    @Autowired
    private OperateLogService operateLogService;

    @RequiresPermissions("monitor:operlog:view")
    @GetMapping
    @ApiOperation("获取操作日志页面")
    public String operlog() {
        return prefix + "/operlog";
    }

    @RequiresPermissions("monitor:operlog:list")
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("获取操作日志列表")
    public TableDataInfo list(OperLog operLog) {
        startPage();
        List<OperLog> list = operateLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:operlog:export")
    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出操作日志")
    public AjaxResult export(OperLog operLog) {
        List<OperLog> list = operateLogService.selectOperLogList(operLog);
        ExcelUtil<OperLog> util = new ExcelUtil<OperLog>(OperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation("删除操作日志")
    public AjaxResult remove(String ids) {
        return toAjax(operateLogService.deleteOperLogByIds(ids));
    }

    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping("/detail/{operId}")
    @ApiOperation("获取操作日志详细信息")
    public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
        mmap.put("operLog", operateLogService.selectOperLogById(operId));
        return prefix + "/detail";
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    @ResponseBody
    @ApiOperation("清空操作日志")
    public AjaxResult clean() {
        operateLogService.cleanOperLog();
        return success();
    }
}
