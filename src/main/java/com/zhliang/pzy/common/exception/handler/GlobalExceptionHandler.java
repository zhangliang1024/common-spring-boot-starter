package com.zhliang.pzy.common.exception.handler;

import com.zhliang.pzy.common.core.annotation.ExceptionCode;
import com.zhliang.pzy.common.core.assertion.enums.ArgumentResponseEnum;
import com.zhliang.pzy.common.core.assertion.enums.CommonResponseEnum;
import com.zhliang.pzy.common.core.assertion.enums.ServletResponseEnum;
import com.zhliang.pzy.common.core.protocol.response.BaseResponse;
import com.zhliang.pzy.common.core.protocol.response.CResponse;
import com.zhliang.pzy.common.core.protocol.response.EResponse;
import com.zhliang.pzy.common.exception.BaseException;
import com.zhliang.pzy.common.exception.BusinessException;
import com.zhliang.pzy.common.exception.i18n.I18nMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Field;

/**
 * 全局异常处理
 */
@ControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(GlobalExceptionHandler.class)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    @Autowired
    private I18nMessageSource i18nMessageSource;

    @Value("${spring.profiles.active:dev}")
    private String profile;

    /**
     * 获取国际化消息
     * @param e 异常
     * @return
     */
    public String getI18nMessage(BaseException e) {
        String code = "response." + e.getResponseEnum().toString();
        String message = i18nMessageSource.getMessage(code, e.getArgs());
        if (message == null || message.isEmpty()) {
            return e.getMessage();
        }
        return message;
    }

    /**
     * 捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public EResponse handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);
        return new EResponse(e.getResponseEnum().getCode(), getI18nMessage(e));
    }

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public EResponse handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);

        return new EResponse(e.getResponseEnum().getCode(), getI18nMessage(e));
    }

    /**
     * 未到达Controller层的相关异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public EResponse handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }
        if (ENV_PROD.equals(profile)) {
            // 为生产环境时, 不适合把具体的异常信息展示给用户, 比如404.
            code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getI18nMessage(baseException);
            return new EResponse(code, message);
        }
        return new EResponse(code, e.getMessage());
    }

    /**
     * 参数绑定异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public EResponse handleBindException(BindException e) {
        log.error("参数绑定异常：{}", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     * TODO: 自定义参数异常信息在这里处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse handleValidException(MethodArgumentNotValidException e) throws NoSuchFieldException {

        // 从异常对象中拿到错误信息
        String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 参数的Class对象，等下好通过字段名称获取Field对象
        Class<?> parameterType = e.getParameter().getParameterType();
        // 拿到错误的字段名称
        String fieldName = e.getBindingResult().getFieldError().getField();
        Field field = parameterType.getDeclaredField(fieldName);
        // 获取Field对象上的自定义注解
        ExceptionCode annotation = field.getAnnotation(ExceptionCode.class);

        // 有注解的话就返回注解的响应信息
        if (annotation != null) {
            return new CResponse(annotation.value(),annotation.message(),defaultMessage);
        }


        log.error("参数绑定校验异常：{}", e);
        return wrapperBindingResult(e.getBindingResult());
    }



    /**
     * 包装绑定异常结果
     */
    private EResponse wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError)error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
        return new EResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }


    /**
     * 未定义异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public EResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            int code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getI18nMessage(baseException);
            return new EResponse(code, message);
        }
        return new EResponse(CommonResponseEnum.SERVER_ERROR.getCode(), e.getMessage());
    }

}
