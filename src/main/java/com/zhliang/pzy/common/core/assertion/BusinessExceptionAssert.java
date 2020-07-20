package com.zhliang.pzy.common.core.assertion;

import com.zhliang.pzy.common.exception.BaseException;
import com.zhliang.pzy.common.exception.BusinessException;

import java.text.MessageFormat;

/**
 * 业务断言异常
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, msg, cause);
    }
}
