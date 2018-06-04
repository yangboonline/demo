package com.bert.jetcache.common;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 返回给调用者的消息(静态方法调用，不提供无参构造器)
 *
 * @author yangbo on 17/5/31.
 */
@Getter
public final class Message implements View {

    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final int RESULT_SUCCESS = 2000;
    private static final int RESULT_FAILED = 3000;
    private static final int PARAM_ERROR = 4000;

    public static final Message REQUEST_ERROR = Message.failed("access data error.");
    public static final Message EMPTY_SUCCESS = success(null);

    // ***********************************message*********************************
    private final int code;
    private final String message;
    private final Object data;

    private Message(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ***********************************method*********************************
    public static Message success(Object data) {
        return Message.success("success", data);
    }

    public static Message success(String message, Object data) {
        return new Message(RESULT_SUCCESS, message, data);
    }

    public static Message failed(String message) {
        if (StringUtils.isBlank(message)) {
            return REQUEST_ERROR;
        }
        return Message.failed(RESULT_FAILED, message);
    }

    public static Message failed(int code, String message) {
        return Message.failed(code, message, null);
    }

    public static Message failed(int code, String message, Object data) {
        return new Message(code, message, data);
    }

    public static Message paramError(String message) {
        return failed(PARAM_ERROR, message);
    }

    // ***********************************implements*********************************
    @Override
    public String getContentType() {
        return JSON_CONTENT_TYPE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(JSON_CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(this));
    }

}
