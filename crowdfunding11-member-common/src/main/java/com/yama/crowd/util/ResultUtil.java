package com.yama.crowd.util;

import com.yama.crowd.constant.CrowdEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规范ajax请求返回json的统一格式，分布式架构中也可以用来调用各个模块时返回统一类型
 */
@Data
@NoArgsConstructor
public class ResultUtil<T> {
    //用来封装当前请求的结果是成功还是失败
    private boolean result;

    //请求失败时，返回的错误信息
    private String message;

    //要返回的数据对象
    private T data;

    public static <T> ResultUtil<T> build(T data){
        ResultUtil<T> resultUtil = new ResultUtil<>();
        resultUtil.setData(data);
        return resultUtil;
    }

    public static <T> ResultUtil<T> build(T data, CrowdEnum crowdEnum){
        ResultUtil<T> build = ResultUtil.build(data);
        build.setResult(crowdEnum.isCode());
        build.setMessage(crowdEnum.getMessage());
        return build;
    }

    public static <T> ResultUtil<T> build(CrowdEnum crowdEnum){
        ResultUtil<T> fail = ResultUtil.build(null);
        fail.setResult(crowdEnum.isCode());
        fail.setMessage(crowdEnum.getMessage());
        return fail;
    }

    public static <T> ResultUtil<T> fail() {
        return ResultUtil.build(CrowdEnum.FAIL);
    }


    public static <T> ResultUtil<T> ok(T data){
        return ResultUtil.build(data, CrowdEnum.OK);
    }

    public static <T> ResultUtil<T> fail(T data){
        return ResultUtil.build(data, CrowdEnum.FAIL);
    }
}
