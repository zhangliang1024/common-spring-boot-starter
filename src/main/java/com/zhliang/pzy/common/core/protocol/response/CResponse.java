package com.zhliang.pzy.common.core.protocol.response;


import com.zhliang.pzy.common.core.assertion.IResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 正常返回结果
 */
@ApiModel
public class CResponse<T> extends BaseResponse {

    @ApiModelProperty(value = "响应的具体数据")
    private T data;

    public CResponse() {
        super();
    }

    public CResponse(T data) {
        super();
        this.data = data;
    }

    public CResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public CResponse(IResponseEnum responseEnum, T data) {
        super(responseEnum);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
