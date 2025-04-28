package com.acc.controller.system.distance;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.WorkgroupLocation;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.IWorkgroupLocationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 工作组物理位置坐标Controller
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Controller
@RequestMapping("/system/location")
public class WorkgroupLocationController extends BaseController {
    private String prefix = "system/location";

    @Autowired
    private IWorkgroupLocationService workgroupLocationService;

    @RequiresPermissions("system:location:view")
    @GetMapping()
    public String location() {
        return prefix + "/location";
    }

    /**
     * 查询工作组物理位置坐标列表
     */
    @RequiresPermissions("system:location:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkgroupLocation workgroupLocation) {
        startPage();
        List<WorkgroupLocation> list = workgroupLocationService.selectWorkgroupLocationList(workgroupLocation);
        return getDataTable(list);
    }

    /**
     * 导出工作组物理位置坐标列表
     */
    @RequiresPermissions("system:location:export")
    @Log(title = "工作组物理位置坐标", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkgroupLocation workgroupLocation) {
        List<WorkgroupLocation> list = workgroupLocationService.selectWorkgroupLocationList(workgroupLocation);
        ExcelUtil<WorkgroupLocation> util = new ExcelUtil<WorkgroupLocation>(WorkgroupLocation.class);
        return util.exportExcel(list, "工作组物理位置坐标数据");
    }

    /**
     * 新增工作组物理位置坐标
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工作组物理位置坐标
     */
    @RequiresPermissions("system:location:add")
    @Log(title = "工作组物理位置坐标", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkgroupLocation workgroupLocation) {
        return toAjax(workgroupLocationService.insertWorkgroupLocation(workgroupLocation));
    }

    /**
     * 修改工作组物理位置坐标
     */
    @RequiresPermissions("system:location:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WorkgroupLocation workgroupLocation =
                workgroupLocationService.selectWorkgroupLocationById(id);
        mmap.put("workgroupLocation", workgroupLocation);
        return prefix + "/edit";
    }

    /**
     * 修改保存工作组物理位置坐标
     */
    @RequiresPermissions("system:location:edit")
    @Log(title = "工作组物理位置坐标", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkgroupLocation workgroupLocation) {
        return toAjax(workgroupLocationService.updateWorkgroupLocation(workgroupLocation));
    }

    /**
     * 删除工作组物理位置坐标
     */
    @RequiresPermissions("system:location:remove")
    @Log(title = "工作组物理位置坐标", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(workgroupLocationService.deleteWorkgroupLocationByIds(ids));
    }

    @GetMapping("/list")
    @ResponseBody
    public List<WorkgroupLocation> getAll() {
        ;
        return workgroupLocationService.getAll();
    }
}
