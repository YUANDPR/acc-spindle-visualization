package com.acc.core.entity;

import com.acc.core.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;


/**
 * 工单工艺流程流转总距离汇总对象 workorder_distance_summary
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public class WorkorderDistanceSummary extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 工单物料号
     */
    @Excel(name = "工单物料号")
    private String materialId;

    /**
     * 总曼哈顿距离
     */
    @Excel(name = "总曼哈顿距离")
    private Long totalManhattanDistance;

    /**
     * 计算时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计算时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date calcTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Long getTotalManhattanDistance() {
        return totalManhattanDistance;
    }

    public void setTotalManhattanDistance(Long totalManhattanDistance) {
        this.totalManhattanDistance = totalManhattanDistance;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("totalManhattanDistance", getTotalManhattanDistance())
                .append("calcTime", getCalcTime())
                .toString();
    }
}
