package com.zhliang.pzy.common.core.assertion;

import com.zhliang.pzy.common.exception.ArgumentException;
import com.zhliang.pzy.common.exception.BaseException;

import java.text.MessageFormat;

/**
 * 一般断言异常
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new ArgumentException(this, args, msg, cause);
    }
}
