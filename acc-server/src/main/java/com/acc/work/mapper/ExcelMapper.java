package com.acc.work.mapper;

import com.acc.data.OrderData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Package com.acc.mapper
 * @ClassName ExcelUploadMapper
 * @Description
 * @Author YUAND
 * @Date 2024/8/21 11:01
 * @Version 1.0
 */
@Mapper
public interface ExcelMapper {

    void insertBatch(List<OrderData> orderData);
}
