package com.zhliang.pzy.common.core.assertion.enums;


import com.zhliang.pzy.common.core.assertion.CommonExceptionAssert;

/**
 * 参数异常枚举定义类
 */
public enum ArgumentResponseEnum implements CommonExceptionAssert {

    VALID_ERROR(500501, "参数校验异常")


    ;

    private int code;

    private String message;

    ArgumentResponseEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
    @Override
    public int getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
