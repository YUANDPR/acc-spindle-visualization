package com.acc.service.impl;


import com.acc.api.jdy.FormApiClient;
import com.acc.api.jdy.FormDataApiClient;
import com.acc.constants.HttpConstant;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import com.acc.mapper.ExecutingOrderMapper;
import com.acc.model.form.FormDataCreateParam;
import com.acc.model.form.FormDataQueryParam;
import com.acc.model.form.FormDataUpdateParam;
import com.acc.model.form.FormQueryParam;
import com.acc.service.JDYService;
import com.acc.service.OrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class JDYServiceImpl implements JDYService {

    private static final String SECRET = "c3LPBsgMxuvfwKmrVEaFFE2n";  // 设置你的密钥
    private static final String APP_ID = "66c98f660039c970b3a1d4f5";
    private static final String ENTRY_ID = "66e016bf16f0a1ea919ced75";

    private final OrderService orderService;
    private final ExecutingOrderMapper executingOrderMapper;
    private static final FormApiClient formApiClient = new FormApiClient(HttpConstant.API_KEY, HttpConstant.HOST);
    private static final FormDataApiClient formDataApiClient = new FormDataApiClient(HttpConstant.API_KEY, HttpConstant.HOST);


    public JDYServiceImpl(OrderService orderService, ExecutingOrderMapper executingOrderMapper) {
        this.orderService = orderService;
        this.executingOrderMapper = executingOrderMapper;
        try {
            testJDY();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void testJDY() throws Exception {
        formWidgets();
        entryList();
    }

    @Override
    public void pushAllData2JDY() throws Exception {
        orderService.getAllExecutingOrders()
                .stream()
                .map(orderService::serializeExecutingOrder)
                .forEach(this::updateExecutingOrder);
    }


    @Override
    public void pullAllDataFromJDY() {
        List<EWResult> allData = getAllData();  // 获取所有数据
        for (EWResult ewResult : allData) {
            try {
                ExecutingOrderDto executingOrderDto = new ExecutingOrderDto();
                executingOrderDto.setId(ewResult.getId());
                executingOrderDto.setOrderId(ewResult.getOrderId());
                executingOrderDto.setExecutingProcedureId(ewResult.getExecutingProcedureId());
                executingOrderDto.setExecuting(ewResult.getExecuting() != null && !ewResult.getExecuting().isEmpty());
                executingOrderDto.setUpdate_time(ewResult.getUpdateTimeValue());

                // 处理订单信息
                ExecutingOrder existingOrder = orderService.getExecutingOrderByOrderId(executingOrderDto.getOrderId());

                if (existingOrder != null) {
                    // 如果订单已存在，则更新订单
                    executingOrderMapper.updateExecutingOrder(executingOrderDto);
                } else {
                    // 如果订单不存在，则插入新订单
                    executingOrderMapper.insertExecutingOrder(executingOrderDto);
                }
            } catch (OrderNotFoundException e) {
                log.error("Order not found for ID: {}", ewResult.getId(), e);
            } catch (Exception e) {
                log.error("Failed to process EWResult with ID: {}", ewResult.getId(), e);
            }
        }
    }


    private List<EWResult> getAllData(){
        FormDataQueryParam formDataQueryParam = new FormDataQueryParam(APP_ID, ENTRY_ID);
        Map<String, Object> map = new HashMap<>();
        try {
            map = formDataApiClient.batchDataQuery(formDataQueryParam, "v5");
        } catch (Exception e) {
            log.error(e.toString());
        }
        return parseEWResults(map);
    }

    public void updateExecutingOrder(ExecutingOrderDto executingOrder) {
        List<EWResult> allData = getAllData();
        boolean flag = false;
        for (EWResult allDatum : allData) {
            if (allDatum.getId() == executingOrder.getId()){
                flag = true;
                allDatum.setOrderId(executingOrder.getOrderId());
                allDatum.setExecutingProcedureId(executingOrder.getExecutingProcedureId());
                allDatum.setUpdateTime(String.valueOf(executingOrder.getUpdate_time()));
                if (executingOrder.isExecuting()){
                    allDatum.setExecuting(Collections.singletonList("executing"));
                } else {
                    allDatum.setExecuting(Collections.emptyList());
                }
                FormDataUpdateParam formDataUpdateParam = new FormDataUpdateParam(APP_ID, ENTRY_ID, serializeEWResult(allDatum));
                log.debug(allDatum.toString());
                try {
                    formDataApiClient.singleDataUpdate(formDataUpdateParam, "v5");
                } catch (Exception e) {
                    log.error(e.toString());
                }

            }
        }
        if (!flag){
            insertExecutingOrder(executingOrder);
        }
    }

    private void insertExecutingOrder(ExecutingOrderDto executingOrder) {
        FormDataCreateParam formDataCreateParam = new FormDataCreateParam(APP_ID, ENTRY_ID, serializeEO2Map(executingOrder));
        try {
            formDataApiClient.singleDataCreate(formDataCreateParam, "v5");
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private static void formWidgets() throws Exception {
        Map<String, Object> result = formApiClient.formWidgets(APP_ID, ENTRY_ID, null);
        log.debug("formWidgets result {}", result);
    }

    private static void entryList() throws Exception {
        FormQueryParam queryParam = new FormQueryParam();
        queryParam.setSkip(0);
        queryParam.setLimit(0);
        queryParam.setApp_id(APP_ID);
        Map<String, Object> result = formApiClient.entryList(queryParam, null);
        log.debug("entryList result {}", result);
    }

    // 签名验证方法
    public boolean validateSignature(String receivedSignature, String nonce, String payload, String timestamp) {
        String calculatedSignature = DigestUtils.sha1Hex(nonce + ":" + payload + ":" + SECRET + ":" + timestamp);
        log.warn("Invalid signature:{}, correct:{}.", calculatedSignature, receivedSignature);
        return calculatedSignature.equals(receivedSignature);
    }

    // 处理更新操作
    public void handleUpdate(String body) throws IllegalOrderCorrespondingQuantityException {
        // 解析请求体并处理
        JSONObject data;
        try {
            JSONObject payloadJSON = new JSONObject(body);
            String op = payloadJSON.getString("op");
            data = payloadJSON.getJSONObject("data");
            log.info("handle Update op:{}", op);
        } catch (JSONException e) {
            log.error(e.getMessage());
            return;
        }

        // 处理数据
        ExecutingOrderDto executingOrderDto = processDataCreate(data);
        try {
            ExecutingOrder executingOrder = orderService.deserializeExecutingOrder(executingOrderDto);
            ExecutingOrderDto byId = executingOrderMapper.getById(executingOrder.getId());

            if (byId != null) {
                executingOrderMapper.updateExecutingOrder(executingOrderDto);
            } else {
                List<ExecutingOrderDto> byOrderId = executingOrderMapper.getByOrderId(executingOrderDto.getOrderId());
                if (byOrderId != null && !byOrderId.isEmpty()) {
                    throw new IllegalOrderCorrespondingQuantityException("发现了该orderId: "+ executingOrder.getOrder().getOrderId() +"对应的多个运行工单！");
                }
                executingOrderMapper.insertExecutingOrder(executingOrderDto);
            }
        } catch (OrderNotFoundException e) {
            log.error("Order not found.", e);
        }

    }


    // 处理数据创建操作
    private ExecutingOrderDto processDataCreate(JSONObject data) {
        // 使用 optString、optInt 等方法来安全提取字段

        // 提取所有字段信息
        String formName = data.optString("formName", null);
        String _id = data.optString("_id", null);
        String appId = data.optString("appId", null);
        String entryId = data.optString("entryId", null);

        // 提取 creator, updater, deleter (json 对象)
        JSONObject creator = data.optJSONObject("creator");
        Result creatorResult = getResult(creator);

        JSONObject updater = data.optJSONObject("updater");
        Result updaterResult = getResult(updater);

        JSONObject deleter = data.optJSONObject("deleter");
        Result deleterResult = getResult(deleter);

        // 提取时间字段 (UTC 时间戳)
        String createTime = data.optString("createTime", null);
        String updateTime = data.optString("updateTime", null);
        String deleteTime = data.optString("deleteTime", null);

        // 提取其他字段
        int id = data.optInt("id", -1);
        String orderId = data.optString("order_id", null);
        String executingProcedureId = data.optString("executing_procedure_id", null);
        boolean executing = data.optJSONArray("executing").length() == 1;  // 数组
        int update_time = data.optInt("update_time", -1);

        // 输出日志
        log.debug("Processing data_create:");
        log.debug("Form Name: {}", formName);
        log.debug("App ID: {}", appId);
        log.debug("Entry ID: {}", entryId);
        log.debug("Creator: {} ({}), Status: {}", creatorResult.creatorName(), creatorResult.creatorUsername(), creatorResult.creatorStatus());
        log.debug("Updater: {} ({}), Status: {}", updaterResult.creatorName(), updaterResult.creatorUsername(), updaterResult.creatorStatus());
        log.debug("Deleter: {} ({}), Status: {}", deleterResult.creatorName(), deleterResult.creatorUsername(), deleterResult.creatorStatus());
        log.debug("Create Time: {}", createTime);
        log.debug("Update Time: {}", updateTime);
        log.debug("Delete Time: {}", deleteTime);
        log.debug("ID: {}", id);
        log.debug("Order ID: {}", orderId);
        log.debug("Executing Procedure ID: {}", executingProcedureId);
        log.debug("Executing: {}", executing);
        log.debug("Update Time: {}", update_time);

        ExecutingOrderDto executingOrderDto = new ExecutingOrderDto();
        executingOrderDto.setId(id);
        executingOrderDto.setOrderId(Integer.parseInt(orderId));
        executingOrderDto.setExecuting(executing);
        executingOrderDto.setUpdate_time((long) update_time);
        executingOrderDto.setExecutingProcedureId(Integer.parseInt(executingProcedureId));

        return executingOrderDto;
    }

    private static Result getResult(JSONObject creator) {
        String creatorName = creator != null ? creator.optString("name", null) : null;
        String creatorUsername = creator != null ? creator.optString("username", null) : null;
        int creatorStatus = creator != null ? creator.optInt("status", -1) : -1;
        return new Result(creatorName, creatorUsername, creatorStatus);
    }

    private record Result(String creatorName, String creatorUsername, int creatorStatus) {
    }

    @Data
    private static class EWResult {
        private User creator;
        private User updater;
        private User deleter;
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


    }
    @Data
    private static class User {
        private String name;
        private String username;
        private int status;
        private int type;
        private List<Integer> departments;
    }


    private List<EWResult> parseEWResults(Map<String, Object> dataMap){

        // 获取 data 对应的列表
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("data");

        // 创建结果的列表
        List<EWResult> ewResults = new ArrayList<>();

        // 遍历列表，并手动解析每个对象
        for (Map<String, Object> item : dataList) {
            EWResult result = new EWResult();

            // 处理 creator 对象
            Map<String, Object> creatorMap = (Map<String, Object>) item.get("creator");
            result.setCreator(parseUser(creatorMap));

            // 处理 updater 对象
            Map<String, Object> updaterMap = (Map<String, Object>) item.get("updater");
            result.setUpdater(parseUser(updaterMap));

            // 处理 deleter 对象 (如果有)
            Map<String, Object> deleterMap = (Map<String, Object>) item.get("deleter");
            if (deleterMap != null) {
                result.setDeleter(parseUser(deleterMap));
            }

            // 设置其他简单的字段
            result.setCreateTime((String) item.get("createTime"));
            result.setUpdateTime((String) item.get("updateTime"));
            result.setDeleteTime((String) item.get("deleteTime"));
            System.out.println(item.get("id"));
            result.setId((Integer) item.get("id"));
            result.setOrderId(Integer.parseInt((String) item.get("order_id")));
            result.setExecutingProcedureId(Integer.parseInt((String) item.get("executing_procedure_id")));
            result.setExecuting((List<Object>) item.get("executing"));
            result.setUpdateTimeValue(Long.valueOf((item.get("update_time").toString())));
            result.set_id((String) item.get("_id"));
            result.setAppId((String) item.get("appId"));
            result.setEntryId((String) item.get("entryId"));

            // 将构建好的 EWResult 对象添加到列表中
            ewResults.add(result);
        }
        return ewResults;
    }

    private Map<String, Object> serializeEWResult(EWResult result) {
        Map<String, Object> item = new HashMap<>();

        // 设置其他简单的字段
        item.put("id", getSimpleValueMap(result.getId()));
        item.put("order_id", getSimpleValueMap(result.getOrderId()));
        item.put("executing_procedure_id", getSimpleValueMap(result.getExecutingProcedureId()));
        item.put("executing", getSimpleValueMap(result.getExecuting()));
        item.put("update_time", getSimpleValueMap(result.getUpdateTime()));

        // 将构建好的 Map 对象添加到列表中
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", item);
        ret.put("_id", result.get_id());
        return ret;
    }

    private Map<String, Object> serializeEO2Map(ExecutingOrderDto result) {
        Map<String, Object> item = new HashMap<>();

        // 设置其他简单的字段
        item.put("id", getSimpleValueMap(result.getId()));
        item.put("order_id", getSimpleValueMap(result.getOrderId()));
        item.put("executing_procedure_id", getSimpleValueMap(result.getExecutingProcedureId()));

        if (result.isExecuting()){
            item.put("executing", Collections.singletonList("executing"));
        } else {
            item.put("executing", Collections.emptyList());
        }
        item.put("update_time", getSimpleValueMap(result.getUpdate_time()));

        // 将构建好的 Map 对象添加到列表中
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", item);
        return ret;
    }

    private Map<String, Object> getSimpleValueMap(Object value){
        Map<String, Object> ret = new HashMap<>();
        ret.put("value", value);
        return ret;
    }

    private Map<String, Object> getOneObjectListValueMap(Object value){
        Map<String, Object> ret = new HashMap<>();
        ret.put("value", List.of(value));
        return ret;
    }


    private User parseUser(Map<String, Object> userMap) {
        User user = new User();
        user.setName((String) userMap.get("name"));
        user.setUsername((String) userMap.get("username"));
        user.setStatus((Integer) userMap.get("status"));
        user.setType((Integer) userMap.get("type"));
        user.setDepartments((List<Integer>) userMap.get("departments"));
        return user;
    }

    private Map<String, Object> serializeUser(User user) {
        Map<String, Object> userMap = new HashMap<>();

        // 将 User 对象的属性存入 Map 中
        userMap.put("name", user.getName());
        userMap.put("username", user.getUsername());
        userMap.put("status", user.getStatus());
        userMap.put("type", user.getType());
        userMap.put("departments", user.getDepartments());

        return userMap;
    }

}
