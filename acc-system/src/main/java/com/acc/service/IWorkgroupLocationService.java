package com.acc.service;

import com.acc.core.entity.WorkgroupLocation;

import java.util.List;


/**
 * 工作组物理位置坐标Service接口
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public interface IWorkgroupLocationService {
    /**
     * 查询工作组物理位置坐标
     *
     * @param id 工作组物理位置坐标主键
     * @return 工作组物理位置坐标
     */
    public WorkgroupLocation selectWorkgroupLocationById(Long id);

    /**
     * 查询工作组物理位置坐标列表
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 工作组物理位置坐标集合
     */
    public List<WorkgroupLocation> selectWorkgroupLocationList(WorkgroupLocation workgroupLocation);

    /**
     * 新增工作组物理位置坐标
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 结果
     */
    public int insertWorkgroupLocation(WorkgroupLocation workgroupLocation);

    /**
     * 修改工作组物理位置坐标
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 结果
     */
    public int updateWorkgroupLocation(WorkgroupLocation workgroupLocation);

    /**
     * 批量删除工作组物理位置坐标
     *
     * @param ids 需要删除的工作组物理位置坐标主键集合
     * @return 结果
     */
    public int deleteWorkgroupLocationByIds(String ids);

    /**
     * 删除工作组物理位置坐标信息
     *
     * @param id 工作组物理位置坐标主键
     * @return 结果
     */
    public int deleteWorkgroupLocationById(Long id);

    List<WorkgroupLocation> getAll();

}
