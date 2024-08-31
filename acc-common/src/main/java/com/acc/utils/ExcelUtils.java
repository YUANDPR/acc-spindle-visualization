package com.acc.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;

/**
 * 读取Excel工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * 根据文件路径读取
     *
     * @param filePath   文件路径
     * @param excelClass 表格对象Class
     * @param <T>        表格对象类型
     * @return 数据集合
     */
    public <T> List<T> readExcelFromPath(String filePath, Class<T> excelClass) {
        List<T> dataList = ListUtils.newArrayList();
        EasyExcel.read(filePath, excelClass, createReadListener(dataList)).sheet().doRead();
        return dataList;
    }

    /**
     * 根据输入流读取
     *
     * @param file       输入流
     * @param excelClass 表格对象Class
     * @param <T>        表格对象类型
     * @return 数据集合
     */
    public <T> List<T> readExcelFromStream(InputStream file, Class<T> excelClass) {
        List<T> dataList = ListUtils.newArrayList();
        EasyExcel.read(file, excelClass, createReadListener(dataList)).sheet().doRead();
        return dataList;
    }

    /**
     * 构造监听器
     *
     * @param dataList 表格对象集合
     * @param <T>      表格对象类型
     * @return 监听器
     */
    private <T> ReadListener<T> createReadListener(List<T> dataList) {
        return new ReadListener<T>() {

            // 每读取一行数据调用一次
            @Override
            public void invoke(T data, AnalysisContext context) {
                dataList.add(data);
            }

            // 读取完所有的数据调用一次
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("sheet={} 所有数据解析完成！", context.readSheetHolder().getSheetName());
            }

        };
    }

}