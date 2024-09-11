package com.acc.core.entity;

import com.acc.core.dto.ExecutingOrderDto;
import com.acc.service.impl.JDYServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class EWResult {
    private JDYUser creator;
    private JDYUser updater;
    private JDYUser deleter;
    private String createTime;
    private String updateTime;
    private String deleteTime;
    private int id;
    private int orderId;
    private int executingProcedureId;
    private List<Object> executing;
    private Long updateTimeValue;
    private String _id;
    private String appId;
    private String entryId;

    public static Map<String, Object> getSimpleValueMap(Object value){
        Map<String, Object> ret = new HashMap<>();
        ret.put("value", value);
        return ret;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> item = new HashMap<>();

        // 设置其他简单的字段
        item.put("id", getSimpleValueMap(this.getId()));
        item.put("order_id", getSimpleValueMap(this.getOrderId()));
        item.put("executing_procedure_id", getSimpleValueMap(this.getExecutingProcedureId()));
        item.put("executing", getSimpleValueMap(this.getExecuting()));
        item.put("update_time", getSimpleValueMap(this.getUpdateTime()));

        // 将构建好的 Map 对象添加到列表中
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", item);
        ret.put("_id", this.get_id());
        return ret;
    }

    public EWResult(Map<String, Object> item) {
        // 处理 creator 对象
        Map<String, Object> creatorMap = JDYServiceImpl.typeConversionMap(item.get("creator"), String.class, Object.class);
        this.setCreator(new JDYUser(creatorMap));

        // 处理 updater 对象
        Map<String, Object> updaterMap = JDYServiceImpl.typeConversionMap(item.get("updater"), String.class, Object.class);
        this.setUpdater(new JDYUser(updaterMap));

        // 处理 deleter 对象 (如果有)
        Object deleter = item.get("deleter");
        Map<String, Object> deleterMap = JDYServiceImpl.typeConversionMap(deleter, String.class, Object.class);
        this.setDeleter(new JDYUser(deleterMap));

        this.setCreateTime((String) item.get("createTime"));
        this.setUpdateTime((String) item.get("updateTime"));
        this.setDeleteTime((String) item.get("deleteTime"));

        this.setId((Integer) item.get("id"));
        this.setOrderId(Integer.parseInt((String) item.get("order_id")));
        this.setExecutingProcedureId(Integer.parseInt((String) item.get("executing_procedure_id")));
        this.setExecuting(JDYServiceImpl.castList(item.get("executing"), Object.class));
        this.setUpdateTimeValue(Long.valueOf((item.get("update_time").toString())));

        this.set_id((String) item.get("_id"));
        this.setAppId((String) item.get("appId"));
        this.setEntryId((String) item.get("entryId"));

    }

    public void adjustFromExecutingOrderDto(ExecutingOrderDto executingOrder) {
        setOrderId(executingOrder.getOrderId());
        setExecutingProcedureId(executingOrder.getExecutingProcedureId());
        setUpdateTime(String.valueOf(executingOrder.getUpdate_time()));
        if (executingOrder.isExecuting()){
            setExecuting(Collections.singletonList("executing"));
        } else {
            setExecuting(Collections.emptyList());
        }
    }
}
