package com.acc.controller.work;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.WorkProcedureService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工序管理Controller
 */
@Controller
@RequestMapping("/system/procedure")
public class WorkProcedureController extends BaseController {
    private final String prefix = "work/procedure";

    @Autowired
    private WorkProcedureService workProcedureService;

    @RequiresPermissions("system:procedure:view")
    @GetMapping()
    public String procedure() {
        return prefix + "/procedure";
    }

    /**
     * 查询工序管理列表
     */
    @RequiresPermissions("system:procedure:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkProcedure workProcedure) {
        startPage();
        List<WorkProcedure> list = workProcedureService.selectWorkProcedureList(workProcedure);
        return getDataTable(list);
    }

    /**
     * 导出工序管理列表
     */
    @RequiresPermissions("system:procedure:export")
    @Log(title = "工序管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkProcedure workProcedure) {
        List<WorkProcedure> list = workProcedureService.selectWorkProcedureList(workProcedure);
        ExcelUtil<WorkProcedure> util = new ExcelUtil<WorkProcedure>(WorkProcedure.class);
        return util.exportEasyExcel(list, "工序管理数据");
    }

    /**
     * 新增工序管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工序管理
     */
    @RequiresPermissions("system:procedure:add")
    @Log(title = "工序管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkProcedure workProcedure) {
        return toAjax(workProcedureService.insertWorkProcedure(workProcedure));
    }

    /**
     * 修改工序管理
     */
    @RequiresPermissions("system:procedure:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WorkProcedure workProcedure =
                workProcedureService.selectWorkProcedureById(id);
        mmap.put("workProcedure", workProcedure);
        return prefix + "/edit";
    }

    /**
     * 修改保存工序管理
     */
    @RequiresPermissions("system:procedure:edit")
    @Log(title = "工序管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkProcedure workProcedure) {
        return toAjax(workProcedureService.updateWorkProcedure(workProcedure));
    }

    /**
     * 删除工序管理
     */
    @RequiresPermissions("system:procedure:remove")
    @Log(title = "工序管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(workProcedureService.deleteWorkProcedureByIds(ids));
    }
}
