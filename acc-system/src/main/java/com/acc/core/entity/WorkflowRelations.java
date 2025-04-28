package com.acc.core.entity;

import com.acc.core.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 工作组间关系一维化对象 workflow_relations
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public class WorkflowRelations extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 源工作组
     */
    @Excel(name = "源工作组")
    private String sourceWorkgroup;

    /**
     * 目标工作组
     */
    @Excel(name = "目标工作组")
    private String targetWorkgroup;

    /**
     * 关系数值（单位需根据业务定义）
     */
    @Excel(name = "关系数值", readConverterExp = "单=位需根据业务定义")
    private Long relationValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceWorkgroup() {
        return sourceWorkgroup;
    }

    public void setSourceWorkgroup(String sourceWorkgroup) {
        this.sourceWorkgroup = sourceWorkgroup;
    }

    public String getTargetWorkgroup() {
        return targetWorkgroup;
    }

    public void setTargetWorkgroup(String targetWorkgroup) {
        this.targetWorkgroup = targetWorkgroup;
    }

    public Long getRelationValue() {
        return relationValue;
    }

    public void setRelationValue(Long relationValue) {
        this.relationValue = relationValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sourceWorkgroup", getSourceWorkgroup())
                .append("targetWorkgroup", getTargetWorkgroup())
                .append("relationValue", getRelationValue())
                .toString();
    }
}
