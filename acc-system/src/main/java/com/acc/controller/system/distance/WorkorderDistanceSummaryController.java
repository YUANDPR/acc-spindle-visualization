package com.acc.controller.system.distance;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkorderDistanceSummary;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.mapper.ContrastTableMapper;
import com.acc.service.IWorkorderDistanceSummaryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单工艺流程流转总距离汇总Controller
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Controller
@RequestMapping("/system/summary")
public class WorkorderDistanceSummaryController extends BaseController {
    private final String prefix = "system/summary";

    @Autowired
    private IWorkorderDistanceSummaryService workorderDistanceSummaryService;

    @Autowired
    private ContrastTableMapper contrastTableMapper;

    @RequiresPermissions("system:summary:view")
    @GetMapping()
    public String summary() {
        return prefix + "/summary";
    }

    /**
     * 查询工单工艺流程流转总距离汇总列表
     */
    @RequiresPermissions("system:summary:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkorderDistanceSummary workorderDistanceSummary) {
        startPage();
        List<WorkorderDistanceSummary> list = workorderDistanceSummaryService.selectWorkorderDistanceSummaryList(workorderDistanceSummary);
        return getDataTable(list);
    }

    /**
     * 导出工单工艺流程流转总距离汇总列表
     */
    @RequiresPermissions("system:summary:export")
    @Log(title = "工单工艺流程流转总距离汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkorderDistanceSummary workorderDistanceSummary) {
        List<WorkorderDistanceSummary> list = workorderDistanceSummaryService.selectWorkorderDistanceSummaryList(workorderDistanceSummary);
        ExcelUtil<WorkorderDistanceSummary> util = new ExcelUtil<WorkorderDistanceSummary>(WorkorderDistanceSummary.class);
        return util.exportExcel(list, "工单工艺流程流转总距离汇总数据");
    }

    /**
     * 新增工单工艺流程流转总距离汇总
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工单工艺流程流转总距离汇总
     */
    @RequiresPermissions("system:summary:add")
    @Log(title = "工单工艺流程流转总距离汇总", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkorderDistanceSummary workorderDistanceSummary) {
        return toAjax(workorderDistanceSummaryService.insertWorkorderDistanceSummary(workorderDistanceSummary));
    }

    /**
     * 修改工单工艺流程流转总距离汇总
     */
    @RequiresPermissions("system:summary:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WorkorderDistanceSummary workorderDistanceSummary =
                workorderDistanceSummaryService.selectWorkorderDistanceSummaryById(id);
        mmap.put("workorderDistanceSummary", workorderDistanceSummary);
        return prefix + "/edit";
    }

    /**
     * 修改保存工单工艺流程流转总距离汇总
     */
    @RequiresPermissions("system:summary:edit")
    @Log(title = "工单工艺流程流转总距离汇总", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkorderDistanceSummary workorderDistanceSummary) {
        return toAjax(workorderDistanceSummaryService.updateWorkorderDistanceSummary(workorderDistanceSummary));
    }

    /**
     * 删除工单工艺流程流转总距离汇总
     */
    @RequiresPermissions("system:summary:remove")
    @Log(title = "工单工艺流程流转总距离汇总", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(workorderDistanceSummaryService.deleteWorkorderDistanceSummaryByIds(ids));
    }

    @GetMapping("/update")
    @ResponseBody
    public AjaxResult update() {
        return toAjax(workorderDistanceSummaryService.update());
    }

    @GetMapping("/account")
    @ResponseBody
    public long[] getAccount() {
        long[] result = new long[2];
        result[0] = contrastTableMapper.selectContrastTableById(1L).getValue();
        result[1] = contrastTableMapper.selectContrastTableById(2L).getValue();
        return result;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<WorkorderDistanceSummary> getAll() {
        return workorderDistanceSummaryService.getAll();
    }
}
