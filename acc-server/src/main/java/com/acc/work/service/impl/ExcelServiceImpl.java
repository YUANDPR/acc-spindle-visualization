package com.acc.work.service.impl;

import com.acc.data.OrderData;
import com.acc.work.mapper.ExcelMapper;
import com.acc.work.service.ExcelService;
import com.acc.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Package com.acc.service.impl
 * @ClassName ExcerServiceImpl
 * @Description
 * @Author YUAND
 * @Date 2024/8/21 10:59
 * @Version 1.0
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelMapper excelMapper;

    @Override
    public void excelUpload(MultipartFile file) throws IOException {
        ExcelUtils readExcelUtil = new ExcelUtils();
        List<OrderData> orderData = readExcelUtil.readExcelFromStream(file.getInputStream(), OrderData.class);
        excelMapper.insertBatch(orderData);
    }
}
