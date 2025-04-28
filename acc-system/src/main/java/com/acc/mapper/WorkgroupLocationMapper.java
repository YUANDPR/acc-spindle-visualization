package com.acc.mapper;

import com.acc.core.entity.WorkgroupLocation;

import java.util.List;


/**
 * 工作组物理位置坐标Mapper接口
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public interface WorkgroupLocationMapper {
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
     * 删除工作组物理位置坐标
     *
     * @param id 工作组物理位置坐标主键
     * @return 结果
     */
    public int deleteWorkgroupLocationById(Long id);

    /**
     * 批量删除工作组物理位置坐标
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkgroupLocationByIds(String[] ids);

    List<WorkgroupLocation> getAll();
}
