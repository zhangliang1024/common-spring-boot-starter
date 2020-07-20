package com.zhliang.pzy.common.exception;


import com.zhliang.pzy.common.core.assertion.IResponseEnum;

/**
 * 参数异常
 * 在处理业务的过程中校验参数出现错误，可以抛出该异常
 * 编写公共代码(如工具类)时，对传入参数校验不通过时，也可以抛出该异常
 */
public class ArgumentException extends BaseException {

    public ArgumentException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public ArgumentException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
