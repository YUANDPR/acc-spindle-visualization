package com.acc.service.impl;

import com.acc.core.entity.WorkgroupLocation;
import com.acc.core.text.Convert;
import com.acc.mapper.WorkgroupLocationMapper;
import com.acc.service.IWorkgroupLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 工作组物理位置坐标Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Service
public class WorkgroupLocationServiceImpl implements IWorkgroupLocationService {
    @Autowired
    private WorkgroupLocationMapper workgroupLocationMapper;

    /**
     * 查询工作组物理位置坐标
     *
     * @param id 工作组物理位置坐标主键
     * @return 工作组物理位置坐标
     */
    @Override
    public WorkgroupLocation selectWorkgroupLocationById(Long id) {
        return workgroupLocationMapper.selectWorkgroupLocationById(id);
    }

    /**
     * 查询工作组物理位置坐标列表
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 工作组物理位置坐标
     */
    @Override
    public List<WorkgroupLocation> selectWorkgroupLocationList(WorkgroupLocation workgroupLocation) {
        return workgroupLocationMapper.selectWorkgroupLocationList(workgroupLocation);
    }

    /**
     * 新增工作组物理位置坐标
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 结果
     */
    @Override
    public int insertWorkgroupLocation(WorkgroupLocation workgroupLocation) {
        return workgroupLocationMapper.insertWorkgroupLocation(workgroupLocation);
    }

    /**
     * 修改工作组物理位置坐标
     *
     * @param workgroupLocation 工作组物理位置坐标
     * @return 结果
     */
    @Override
    public int updateWorkgroupLocation(WorkgroupLocation workgroupLocation) {
        return workgroupLocationMapper.updateWorkgroupLocation(workgroupLocation);
    }

    /**
     * 批量删除工作组物理位置坐标
     *
     * @param ids 需要删除的工作组物理位置坐标主键
     * @return 结果
     */
    @Override
    public int deleteWorkgroupLocationByIds(String ids) {
        return workgroupLocationMapper.deleteWorkgroupLocationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工作组物理位置坐标信息
     *
     * @param id 工作组物理位置坐标主键
     * @return 结果
     */
    @Override
    public int deleteWorkgroupLocationById(Long id) {
        return workgroupLocationMapper.deleteWorkgroupLocationById(id);
    }

    /**
     * 获取全部数据
     */
    @Override
    public List<WorkgroupLocation> getWorksetAll() {
        return workgroupLocationMapper.getWorksetAll();
    }

    /**
     * 更新坐标值
     */
    @Override
    public int update() {
        return workgroupLocationMapper.update();
    }
}
