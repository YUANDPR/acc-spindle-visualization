package com.acc.controller.system.system;

import com.acc.controller.system.BaseController;
import com.acc.core.annotation.Log;
import com.acc.core.entity.Role;
import com.acc.core.entity.User;
import com.acc.core.entity.UserRole;
import com.acc.core.entity.Ztree;
import com.acc.core.enumeration.BusinessType;
import com.acc.core.exception.user.ServiceException;
import com.acc.core.page.TableDataInfo;
import com.acc.core.result.AjaxResult;
import com.acc.core.utils.AuthorizationUtils;
import com.acc.core.utils.ExcelUtil;
import com.acc.service.DeptService;
import com.acc.service.RoleService;
import com.acc.service.UserService;
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
 * 角色信息
 */
@Controller
@RequestMapping("/system/role")
@Api(tags = "角色管理")
public class RoleController extends BaseController {
    private String prefix = "system/role";

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @RequiresPermissions("system:role:view")
    @GetMapping
    @ApiOperation("获取角色管理页面")
    public String role() {
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("获取角色列表")
    public TableDataInfo list(Role role) {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出角色信息")
    public AjaxResult export(Role role) {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    @ApiOperation("获取新增角色页面")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("新增并保存角色")
    public AjaxResult addSave(@Validated Role role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getLoginName());
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/edit/{roleId}")
    @ApiOperation("获取修改角色页面")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        roleService.checkRoleDataScope(roleId);
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @ApiOperation("修改并保存角色")
    public AjaxResult editSave(@Validated Role role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getLoginName());
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 角色分配数据权限
     */
    @GetMapping("/authDataScope/{roleId}")
    @ApiOperation("获取分配角色数据权限页面")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }

    /**
     * 保存角色分配数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    @ApiOperation("修改并保存角色数据权限")
    public AjaxResult authDataScopeSave(Role role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getLoginName());
        if (roleService.authDataScope(role) > 0) {
            setUser(userService.selectUserById(getUserId()));
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation("删除角色")
    public AjaxResult remove(String ids) {
        return toAjax(roleService.deleteRoleByIds(ids));
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    @ApiOperation("校验角色名称")
    public boolean checkRoleNameUnique(Role role) {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    @ApiOperation("校验角色权限")
    public boolean checkRoleKeyUnique(Role role) {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择角色树
     */
    @GetMapping("/selectMenuTree")
    @ApiOperation("获取角色树")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 角色状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    @ApiOperation("修改角色状态")
    public AjaxResult changeStatus(Role role) {
        try {
            roleService.checkRoleAllowed(role);
            roleService.checkRoleDataScope(role.getRoleId());
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 分配用户
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/authUser/{roleId}")
    @ApiOperation("获取分配用户页面")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    @ApiOperation("获取已分配用户角色列表")
    public TableDataInfo allocatedList(User user) {
        startPage();
        List<User> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    @ApiOperation("取消角色授权")
    public AjaxResult cancelAuthUser(UserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    @ApiOperation("批量取消角色授权")
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    @ApiOperation("获取选择用户页面")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    @ApiOperation("获取为分配用户角色列表")
    public TableDataInfo unallocatedList(User user) {
        startPage();
        List<User> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    @ApiOperation("批量选择用户授权")
    public AjaxResult selectAuthUserAll(Long roleId, String userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/deptTreeData")
    @ResponseBody
    @ApiOperation("获取角色树")
    public List<Ztree> deptTreeData(Role role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }
}