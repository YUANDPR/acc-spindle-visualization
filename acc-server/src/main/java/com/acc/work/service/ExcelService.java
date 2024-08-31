package com.acc.work.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Package com.acc.service
 * @ClassName ExcelService
 * @Description
 * @Author YUAND
 * @Date 2024/8/21 10:58
 * @Version 1.0
 */
public interface ExcelService {
    void excelUpload(MultipartFile file) throws IOException;
}
