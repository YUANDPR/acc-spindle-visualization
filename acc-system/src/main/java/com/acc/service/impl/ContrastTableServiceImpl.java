package com.acc.service.impl;

import com.acc.core.entity.ContrastTable;
import com.acc.core.text.Convert;
import com.acc.mapper.ContrastTableMapper;
import com.acc.service.IContrastTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 对比Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Service
public class ContrastTableServiceImpl implements IContrastTableService {
    @Autowired
    private ContrastTableMapper contrastTableMapper;

    /**
     * 查询对比
     *
     * @param id 对比主键
     * @return 对比
     */
    @Override
    public ContrastTable selectContrastTableById(Long id) {
        return contrastTableMapper.selectContrastTableById(id);
    }

    /**
     * 查询对比列表
     *
     * @param contrastTable 对比
     * @return 对比
     */
    @Override
    public List<ContrastTable> selectContrastTableList(ContrastTable contrastTable) {
        return contrastTableMapper.selectContrastTableList(contrastTable);
    }

    /**
     * 新增对比
     *
     * @param contrastTable 对比
     * @return 结果
     */
    @Override
    public int insertContrastTable(ContrastTable contrastTable) {
        return contrastTableMapper.insertContrastTable(contrastTable);
    }

    /**
     * 修改对比
     *
     * @param contrastTable 对比
     * @return 结果
     */
    @Override
    public int updateContrastTable(ContrastTable contrastTable) {
        return contrastTableMapper.updateContrastTable(contrastTable);
    }

    /**
     * 批量删除对比
     *
     * @param ids 需要删除的对比主键
     * @return 结果
     */
    @Override
    public int deleteContrastTableByIds(String ids) {
        return contrastTableMapper.deleteContrastTableByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除对比信息
     *
     * @param id 对比主键
     * @return 结果
     */
    @Override
    public int deleteContrastTableById(Long id) {
        return contrastTableMapper.deleteContrastTableById(id);
    }
}
