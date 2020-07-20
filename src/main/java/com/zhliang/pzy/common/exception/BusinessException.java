package com.zhliang.pzy.common.exception;

import com.zhliang.pzy.common.core.assertion.IResponseEnum;

/**
 * 业务异常
 * 业务处理时，出现异常，可以抛出该异常
 */
public class BusinessException extends BaseException {

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
