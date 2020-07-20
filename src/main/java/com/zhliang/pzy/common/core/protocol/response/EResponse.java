package com.zhliang.pzy.common.core.protocol.response;

import com.zhliang.pzy.common.core.assertion.IResponseEnum;
import io.swagger.annotations.ApiModel;


/**
 * 异常返回结果
 */
@ApiModel
public class EResponse extends BaseResponse {

    public EResponse() {
    }

    public EResponse(IResponseEnum responseEnum) {
        super(responseEnum);
    }

    public EResponse(int code, String message) {
        super(code, message);
    }

}
