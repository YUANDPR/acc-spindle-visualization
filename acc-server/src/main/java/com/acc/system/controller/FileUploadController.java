package com.acc.system.controller;

import com.acc.result.AjaxResult;
import com.acc.work.service.ExcelService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传接口
 */
@RestController
@Slf4j
@RequestMapping("/file")
@Api(tags = "文件上传接口")
public class FileUploadController {

    @Autowired
    private ExcelService excelService;

    @PostMapping
    public AjaxResult fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("接收到文件：{}", file.getOriginalFilename());
        excelService.excelUpload(file);
        return AjaxResult.success();
    }
}
