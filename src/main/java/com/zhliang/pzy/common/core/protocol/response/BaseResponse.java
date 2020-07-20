package com.zhliang.pzy.common.core.protocol.response;

import com.zhliang.pzy.common.core.assertion.IResponseEnum;
import com.zhliang.pzy.common.core.assertion.enums.CommonResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基础返回结果
 */
@ApiModel
public class BaseResponse {

    /**
     * 返回码
     */
    @ApiModelProperty(value = "状态码", notes = "默认200是成功")
    protected int code;
    /**
     * 返回消息
     */
    @ApiModelProperty(value = "响应信息", notes = "来说明响应情况")
    protected String message;

    public BaseResponse() {
        //默认创建成功的响应
        this(CommonResponseEnum.SUCCESS);
    }

    public BaseResponse(IResponseEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMessage());
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
