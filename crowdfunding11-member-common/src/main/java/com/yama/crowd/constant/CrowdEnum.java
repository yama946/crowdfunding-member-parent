package com.yama.crowd.constant;

import lombok.Getter;

/**
 * 枚举类
 */
@Getter
public enum CrowdEnum {
    OK(true,"请求成功"),
    FAIL(false,"请求失败");

    private boolean code;

    private String message;

    CrowdEnum(boolean code, String message){
        this.code = code;
        this.message = message;
    }
}
