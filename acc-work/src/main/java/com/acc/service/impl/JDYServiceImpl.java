package com.acc.service.impl;


import com.acc.api.jdy.FormApiClient;
import com.acc.api.jdy.FormDataApiClient;
import com.acc.constants.HttpConstant;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.EWResult;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JDY 服务实现类，负责与 JDY API 交互并处理执行订单相关的操作。
 */
@Slf4j
@Service
public class JDYServiceImpl implements JDYService {

    private static final FormApiClient formApiClient = new FormApiClient(HttpConstant.API_KEY, HttpConstant.HOST);
    private static final FormDataApiClient formDataApiClient = new FormDataApiClient(HttpConstant.API_KEY, HttpConstant.HOST);
    /**
     * 密钥，用于生成签名。
     */
    @Value("${jdy.secret}")
    private static String SECRET;
    /**
     * JDY 应用 ID。
     */
    @Value("${jdy.app-id}")
    private static String APP_ID;
    /**
     * JDY 条目 ID。
     */
    @Value("${jdy.entry-id}")
    private static String ENTRY_ID;
    private final OrderService orderService;
    private final ExecutingOrderMapper executingOrderMapper;

    /**
     * 构造方法，注入依赖并测试与 JDY API 的连接。
     *
     * @param orderService         订单服务接口
     * @param executingOrderMapper 执行订单映射器
     */
    public JDYServiceImpl(OrderService orderService, ExecutingOrderMapper executingOrderMapper) {
        this.orderService = orderService;
        this.executingOrderMapper = executingOrderMapper;
        try {
//            testJDY();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为指定类型的列表。
     *
     * @param obj   原对象
     * @param clazz 目标类的类型
     * @param <T>   目标类型
     * @return 转换后的列表
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
        }
        return result;
    }

    /**
     * 将 Map 的键值对转换为指定类型。
     *
     * @param obj    原对象
     * @param tClass 目标键类型
     * @param vClass 目标值类型
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 转换后的 Map
     */
    public static <K, V> Map<K, V> typeConversionMap(Object obj, Class<K> tClass, Class<V> vClass) {
        HashMap<K, V> result = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) (obj);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(tClass.cast(entry.getKey()), vClass.cast(entry.getValue()));
            }
        }
        return result;
    }

    /**
     * 测试与 JDY API 的连接，输出表单组件和条目列表。
     *
     * @throws Exception 发生错误时抛出异常
     */
    private void testJDY() throws Exception {
        log.debug("formWidgets result {}", formApiClient.formWidgets(APP_ID, ENTRY_ID, null));
        FormQueryParam queryParam = new FormQueryParam();
        queryParam.setSkip(0);
        queryParam.setLimit(0);
        queryParam.setApp_id(APP_ID);
        log.debug("entryList result {}", formApiClient.entryList(queryParam, null));
    }

    /**
     * 将所有执行中的订单数据推送到 JDY。
     *
     * @throws Exception 发生错误时抛出异常
     */
    @Override
    public void pushAllData2JDY() throws Exception {
        orderService.getAllExecutingOrders()
                .stream()
                .map(orderService::serializeExecutingOrder)
                .forEach(this::updateExecutingOrderToJDY);
    }

    /**
     * 从 JDY 获取所有数据并将其与本地订单数据合并，更新或插入新的订单。
     */
    @Override
    public void pullAndMergeAllDataFromJDY() {
        List<EWResult> allData = getAllDataFromJDY();
        allData.forEach(ewResult -> {
            try {
                ExecutingOrderDto executingOrderDto = new ExecutingOrderDto(ewResult);
                ExecutingOrder existingOrder = orderService.getExecutingOrderByOrderId(executingOrderDto.getOrderId());
                if (existingOrder != null) executingOrderMapper.updateExecutingOrder(executingOrderDto);
                else executingOrderMapper.insertExecutingOrder(executingOrderDto);
            } catch (OrderNotFoundException e) {
                log.error("Order not found for ID: {}", ewResult.getId(), e);
            } catch (Exception e) {
                log.error("Failed to process EWResult with ID: {}", ewResult.getId(), e);
            }
        });
    }

    /**
     * 验证签名是否正确。
     *
     * @param receivedSignature 接收到的签名
     * @param nonce             随机数
     * @param payload           负载
     * @param timestamp         时间戳
     * @return 如果签名匹配，则返回 true；否则返回 false
     */
    @Override
    public boolean validateSignature(String receivedSignature, String nonce, String payload, String timestamp) {
        String calculatedSignature = DigestUtils.sha1Hex(nonce + ":" + payload + ":" + SECRET + ":" + timestamp);
        log.warn("Invalid signature:{}, correct:{}.", calculatedSignature, receivedSignature);
        return calculatedSignature.equals(receivedSignature);
    }

    /**
     * 处理来自外部系统的订单更新操作。
     *
     * @param body 更新请求体，包含操作类型和数据
     * @throws IllegalOrderCorrespondingQuantityException 如果订单ID对应多个工单，抛出异常
     */
    @Override
    public void handleUpdate(String body) throws IllegalOrderCorrespondingQuantityException {
        ExecutingOrderDto executingOrderDto = null;

        try {
            // 解析请求体
            JSONObject payloadJSON = new JSONObject(body);
            log.info("handle Update op:{}", payloadJSON.getString("op"));

            // 转换数据
            executingOrderDto = new ExecutingOrderDto(payloadJSON.getJSONObject("data"));
            ExecutingOrder executingOrder = orderService.deserializeExecutingOrder(executingOrderDto);

            // 查找订单是否存在
            if (Optional.ofNullable(executingOrderMapper.getById(executingOrder.getId())).isPresent()) {
                // 更新现有订单
                executingOrderMapper.updateExecutingOrder(executingOrderDto);
            } else {
                // 按 orderId 查找订单，避免出现重复订单
                List<ExecutingOrderDto> ordersByOrderId = executingOrderMapper.getByOrderId(executingOrderDto.getOrderId());
                if (ordersByOrderId != null && !ordersByOrderId.isEmpty()) {
                    throw new IllegalOrderCorrespondingQuantityException(
                            String.format("发现了该orderId: %s 对应的多个运行工单！", executingOrder.getOrder().getOrderId())
                    );
                }

                // 插入新订单
                executingOrderMapper.insertExecutingOrder(executingOrderDto);
            }
        } catch (JSONException e) {
            log.error("Failed to parse JSON body: {}", e.getMessage(), e);
        } catch (OrderNotFoundException e) {
            log.error("Order not found for ID: {}", e.getMessage(), e);
        } catch (IllegalOrderCorrespondingQuantityException e) {
            log.error("Multiple orders found for orderId: {}", executingOrderDto.getOrderId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while handling update: {}", e.getMessage(), e);
        }
    }

    /**
     * 从 JDY 获取所有数据并将其转换为 EWResult 对象列表。
     *
     * @return 转换后的 EWResult 列表
     */
    private List<EWResult> getAllDataFromJDY() {
        try {
            return castList(formDataApiClient
                    .batchDataQuery(new FormDataQueryParam(APP_ID, ENTRY_ID), "v5")
                    .get("data"), Object.class)
                    .stream()
                    .map(datum -> typeConversionMap(datum, String.class, Object.class))
                    .map(EWResult::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.toString());
            return new ArrayList<>();
        }
    }

    /**
     * 将执行订单更新到 JDY 系统中。如果订单 ID 已存在，则更新；否则插入新订单。
     *
     * @param executingOrder 执行订单对象
     */
    public void updateExecutingOrderToJDY(ExecutingOrderDto executingOrder) {

        // 使用流式处理查找是否有匹配的ID
        Optional<EWResult> matchingDatum = getAllDataFromJDY()
                .stream()
                .filter(allDatum -> allDatum.getId() == executingOrder.getId())
                .findFirst();

        if (matchingDatum.isPresent()) {
            // 如果存在匹配的 ID，则更新
            EWResult existingData = matchingDatum.get();
            existingData.adjustFromExecutingOrderDto(executingOrder);

            try {
                FormDataUpdateParam formDataUpdateParam = new FormDataUpdateParam(APP_ID, ENTRY_ID, existingData.toMap());
                formDataApiClient.singleDataUpdate(formDataUpdateParam, "v5");
            } catch (Exception e) {
                log.error("Error updating data: {}", e.getMessage(), e);
            }
        } else {
            // 如果不存在匹配的 ID，则插入新的数据
            try {
                FormDataCreateParam formDataCreateParam = new FormDataCreateParam(APP_ID, ENTRY_ID, executingOrder.toMap());
                formDataApiClient.singleDataCreate(formDataCreateParam, "v5");
            } catch (Exception e) {
                log.error("Error inserting data: {}", e.getMessage(), e);
            }
        }
    }

}
