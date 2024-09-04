package com.acc.controller.system.system;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.DictData;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.DictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 */
@Controller
@RequestMapping("/system/dict/data")
@Api(tags = "字典数据")
public class DictDataController extends BaseController {
    private String prefix = "system/dict/data";

    @Autowired
    private DictDataService dictDataService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    @ApiOperation("获取字典数据页面")
    public String dictData() {
        return prefix + "/data";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    @ApiOperation("获取字典数据列表")
    public TableDataInfo list(DictData dictData) {
        startPage();
        List<DictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出字典数据")
    public AjaxResult export(DictData dictData) {
        List<DictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<DictData> util = new ExcelUtil<DictData>(DictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add/{dictType}")
    @ApiOperation("获取新增字典数据页面")
    public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
        mmap.put("dictType", dictType);
        return prefix + "/add";
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("新增并保存字典数据")
    public AjaxResult addSave(@Validated DictData dict) {
        dict.setCreateBy(getLoginName());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改字典类型
     */
    @RequiresPermissions("system:dict:edit")
    @GetMapping("/edit/{dictCode}")
    @ApiOperation("获取修改字典数据页面")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
        mmap.put("dict", dictDataService.selectDictDataById(dictCode));
        return prefix + "/edit";
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    @ApiOperation("修改并保存字典数据")
    public AjaxResult editSave(@Validated DictData dict) {
        dict.setUpdateBy(getLoginName());
        return toAjax(dictDataService.updateDictData(dict));
    }

    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation("删除字典数据")
    public AjaxResult remove(String ids) {
        dictDataService.deleteDictDataByIds(ids);
        return success();
    }
}
