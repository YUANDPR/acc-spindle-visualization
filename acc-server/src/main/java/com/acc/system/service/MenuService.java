package com.acc.system.service;

import com.acc.entity.system.Menu;
import com.acc.entity.system.Role;
import com.acc.entity.system.User;
import com.acc.entity.system.Ztree;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Package com.acc.service
 * @ClassName MenuService
 * @Description
 * @Author YUAND
 * @Date 2024/8/24 17:22
 * @Version 1.0
 */
public interface MenuService {
    /**
     * 根据用户ID查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    List<Menu> selectMenusByUser(User user);

    /**
     * 查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuList(Menu menu, Long userId);

    /**
     * 查询菜单集合
     *
     * @param userId 用户ID
     * @return 所有菜单信息
     */
    List<Menu> selectMenuAll(Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    Set<String> selectPermsByRoleId(Long roleId);

    /**
     * 根据角色ID查询菜单
     *
     * @param role   角色对象
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Ztree> roleMenuTreeData(Role role, Long userId);

    /**
     * 查询所有菜单信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Ztree> menuTreeData(Long userId);

    /**
     * 查询系统所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Map<String, String> selectPermsAll(Long userId);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    Menu selectMenuById(Long menuId);

    /**
     * 查询菜单数量
     *
     * @param parentId 菜单父ID
     * @return 结果
     */
    int selectCountMenuByParentId(Long parentId);

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int selectCountRoleMenuByMenuId(Long menuId);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(Menu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(Menu menu);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean checkMenuNameUnique(Menu menu);
}
