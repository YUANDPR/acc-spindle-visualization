package com.acc.mapper;

import com.acc.core.entity.ContrastTable;

import java.util.List;


/**
 * 对比Mapper接口
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public interface ContrastTableMapper {
    /**
     * 查询对比
     *
     * @param id 对比主键
     * @return 对比
     */
    public ContrastTable selectContrastTableById(Long id);

    /**
     * 查询对比列表
     *
     * @param contrastTable 对比
     * @return 对比集合
     */
    public List<ContrastTable> selectContrastTableList(ContrastTable contrastTable);

    /**
     * 新增对比
     *
     * @param contrastTable 对比
     * @return 结果
     */
    public int insertContrastTable(ContrastTable contrastTable);

    /**
     * 修改对比
     *
     * @param contrastTable 对比
     * @return 结果
     */
    public int updateContrastTable(ContrastTable contrastTable);

    /**
     * 删除对比
     *
     * @param id 对比主键
     * @return 结果
     */
    public int deleteContrastTableById(Long id);

    /**
     * 批量删除对比
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContrastTableByIds(String[] ids);
}
