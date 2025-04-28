package com.acc.core.entity;

import com.acc.core.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 工作组物理位置坐标对象 workgroup_location
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public class WorkgroupLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 工作组名称
     */
    @Excel(name = "工作组名称")
    private String workgroupName;

    /**
     * 横坐标
     */
    @Excel(name = "横坐标")
    private Long coordX;

    /**
     * 纵坐标
     */
    @Excel(name = "纵坐标")
    private Long coordY;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public Long getCoordX() {
        return coordX;
    }

    public void setCoordX(Long coordX) {
        this.coordX = coordX;
    }

    public Long getCoordY() {
        return coordY;
    }

    public void setCoordY(Long coordY) {
        this.coordY = coordY;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("workgroupName", getWorkgroupName())
                .append("coordX", getCoordX())
                .append("coordY", getCoordY())
                .toString();
    }
}
