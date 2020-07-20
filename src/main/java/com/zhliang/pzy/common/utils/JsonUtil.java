package com.zhliang.pzy.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.json.JsonSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串
     */
    public static String objectToStr(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("json parse error : {}",e.getMessage());
            //throw new BaseException("json parse erro");
            throw new RuntimeException("json parse erro");
        }
    }

    /**
     * 将json结果集转化为对象
     */
    public static <T> T jsonToPo(String json, Class<T> bean) {
        String jsonData = JsonSanitizer.sanitize(json);
        try {
            return MAPPER.readValue(jsonData, bean);
        } catch (Exception e) {
            logger.error("json parse error : {}",e.getMessage());
            //throw new BaseRuntimeException("json parse erro");
            throw new RuntimeException("json parse erro");
        }
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String json, Class<T> bean) {
        String jsonData = JsonSanitizer.sanitize(json);
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, bean);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error("json parse error : {}",e.getMessage());
            //throw new BaseRuntimeException("json parse erro");
            throw new RuntimeException("json parse erro");
        }
    }

}
