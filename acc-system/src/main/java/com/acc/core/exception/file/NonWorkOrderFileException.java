package com.acc.core.exception.file;

/**
 * 上传文件非工单异常
 */
public class NonWorkOrderFileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NonWorkOrderFileException(String msg) {
        super(msg);
    }

}
