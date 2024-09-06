package com.acc.controller.work;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkOrder;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产工单管理
 */
@Controller
@RequestMapping("/system/order")
@Slf4j
@Api(tags = "生产工单管理")
public class WorkOrderController extends BaseController {
    private String prefix = "work/order";

    @Autowired
    private WorkOrderService workOrderService;

    @RequiresPermissions("system:order:view")
    @GetMapping
    @ApiOperation("获取工单管理页面")
    public String order() {
        return prefix + "/order";
    }

    /**
     * 查询生产工单列表
     */
    @RequiresPermissions("system:order:list")
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("查询工单列表")
    public TableDataInfo list(WorkOrder workOrder) {
        startPage();
        List<WorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        return getDataTable(list);
    }

    /**
     * 导出生产工单列表
     */
    @RequiresPermissions("system:order:export")
    @Log(title = "生产工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出工单列表")
    public AjaxResult export(WorkOrder workOrder) {
        List<WorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        ExcelUtil<WorkOrder> util = new ExcelUtil<WorkOrder>(WorkOrder.class);
        return util.exportEasyExcelWithTemplate(list, "生产工单数据", "工单表模板.xlsx");
    }

    /**
     * 新增生产工单
     */
    @GetMapping("/add")
    @ApiOperation("获取新增工单页面")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存生产工单
     */
    @RequiresPermissions("system:order:add")
    @Log(title = "生产工单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("新增并保存工单")
    public AjaxResult addSave(WorkOrder workOrder) {
        return toAjax(workOrderService.insertWorkOrder(workOrder));
    }

    /**
     * 修改生产工单
     */
    @RequiresPermissions("system:order:edit")
    @GetMapping("/edit/{id}")
    @ApiOperation("获取修改工单页面")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WorkOrder workOrder =
                workOrderService.selectWorkOrderById(id);
        mmap.put("workOrder", workOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存生产工单
     */
    @RequiresPermissions("system:order:edit")
    @Log(title = "生产工单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @ApiOperation("修改并保存工单")
    public AjaxResult editSave(WorkOrder workOrder) {
        return toAjax(workOrderService.updateWorkOrder(workOrder));
    }

    /**
     * 删除生产工单
     */
    @RequiresPermissions("system:order:remove")
    @Log(title = "生产工单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation("删除工单")
    public AjaxResult remove(String ids) {
        return toAjax(workOrderService.deleteWorkOrderByIds(ids));
    }
}
