package com.acc.controller.work;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkOrder;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.WorkOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产工单Controller
 */
@Controller
@RequestMapping("/system/order")
public class WorkOrderController extends BaseController {
    private String prefix = "work/order";

    @Autowired
    private WorkOrderService workOrderService;

    @RequiresPermissions("system:order:view")
    @GetMapping()
    public String order() {
        return prefix + "/order";
    }

        /**
         * 查询生产工单列表
         */
        @RequiresPermissions("system:order:list")
        @PostMapping("/list")
        @ResponseBody
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
    public AjaxResult export(WorkOrder workOrder) {
        List<WorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        ExcelUtil<WorkOrder> util = new ExcelUtil<WorkOrder>(WorkOrder. class);
        return util.exportExcel(list, "生产工单数据");
    }

        /**
         * 新增生产工单
         */
        @GetMapping("/add")
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
    public AjaxResult addSave(WorkOrder workOrder) {
        return toAjax(workOrderService.insertWorkOrder(workOrder));
    }

    /**
     * 修改生产工单
     */
    @RequiresPermissions("system:order:edit")
    @GetMapping("/edit/{id}")
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
        public AjaxResult remove(String ids) {
            return toAjax(workOrderService.deleteWorkOrderByIds(ids));
        }
}
