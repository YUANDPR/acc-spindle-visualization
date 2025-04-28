package com.acc.controller.system.distance;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkflowRelations;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.IWorkflowRelationsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 工作组间关系一维化Controller
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Controller
@RequestMapping("/system/relations")
public class WorkflowRelationsController extends BaseController {
    private String prefix = "system/relations";

    @Autowired
    private IWorkflowRelationsService workflowRelationsService;

    @RequiresPermissions("system:relations:view")
    @GetMapping()
    public String relations() {
        return prefix + "/relations";
    }

    /**
     * 查询工作组间关系一维化列表
     */
    @RequiresPermissions("system:relations:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkflowRelations workflowRelations) {
        startPage();
        List<WorkflowRelations> list = workflowRelationsService.selectWorkflowRelationsList(workflowRelations);
        return getDataTable(list);
    }

    /**
     * 导出工作组间关系一维化列表
     */
    @RequiresPermissions("system:relations:export")
    @Log(title = "工作组间关系一维化", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkflowRelations workflowRelations) {
        List<WorkflowRelations> list = workflowRelationsService.selectWorkflowRelationsList(workflowRelations);
        ExcelUtil<WorkflowRelations> util = new ExcelUtil<WorkflowRelations>(WorkflowRelations.class);
        return util.exportExcel(list, "工作组间关系一维化数据");
    }

    /**
     * 新增工作组间关系一维化
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工作组间关系一维化
     */
    @RequiresPermissions("system:relations:add")
    @Log(title = "工作组间关系一维化", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkflowRelations workflowRelations) {
        return toAjax(workflowRelationsService.insertWorkflowRelations(workflowRelations));
    }

    /**
     * 修改工作组间关系一维化
     */
    @RequiresPermissions("system:relations:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WorkflowRelations workflowRelations =
                workflowRelationsService.selectWorkflowRelationsById(id);
        mmap.put("workflowRelations", workflowRelations);
        return prefix + "/edit";
    }

    /**
     * 修改保存工作组间关系一维化
     */
    @RequiresPermissions("system:relations:edit")
    @Log(title = "工作组间关系一维化", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkflowRelations workflowRelations) {
        return toAjax(workflowRelationsService.updateWorkflowRelations(workflowRelations));
    }

    /**
     * 删除工作组间关系一维化
     */
    @RequiresPermissions("system:relations:remove")
    @Log(title = "工作组间关系一维化", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(workflowRelationsService.deleteWorkflowRelationsByIds(ids));
    }
}
