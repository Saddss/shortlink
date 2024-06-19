package com.saddss.shortlink.admin.common.enums;

import com.saddss.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {

    USER_NULL("B000200", "用户记录不存在"),

    USER_NAME_EXIST("B000201", "用户名已存在"),

    USER_EXIST("B000202", "用户记录已存在"),

    USER_SAVE_ERROR("B000203", "用户记录新增失败"),

    USERNAME_OR_PASSWORD_ERROR("B000204", "用户名或密码错误"),

    USER_HAS_LOGGED("B000205", "用户已登录"),

    USER_UNLOGIN_OR_TOKEN_IS_NOT_VALID("B000206", "用户未登录或token无效");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
