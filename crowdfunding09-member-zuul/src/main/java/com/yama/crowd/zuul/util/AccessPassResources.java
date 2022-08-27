package com.yama.crowd.zuul.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 定义不需要通过zuul过滤器的路径
 */
public class AccessPassResources {
    /**
     * 我们希望集合中的数据，初始化类时就添加好，因此我们配置到静态代码块中
     */
    public static final Set<String> PASS_RES_SET = new HashSet<>();
    //允许通过的路径
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        PASS_RES_SET.add("/project/get/project/detail/*");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();
    //允许通过的静态资源
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }
    /**
     * 用于判断某个ServletPath 值是否对应一个静态资源
     * @param servletPath
     * @return
     * true：是静态资源
     * false：不是静态资源
     */
    public static boolean judgeCurrentServletPathWetherStaticResource(String servletPath) {
        // 1.排除字符串无效的情况
        if(servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException("字符串不合法!");
        }
        // 2.根据“/”拆分ServletPath 字符串
        String[] split = servletPath.split("/");
        // 3.考虑到第一个斜杠左边经过拆分后得到一个空字符串是数组的第一个元素，所以需要使用下标1 取第二个元素
        String firstLevelPath = split[1];
        // 4.判断是否在集合中
        return STATIC_RES_SET.contains(firstLevelPath);
    }

    public static void main(String[] args) {
        //分割字符串
        String str1 = "/aaa/bbb/ccc";
        String str2 = "name/yan/age/";
        //分割字符串
        String[] result1 = str1.split("/");
        String[] result2 = str2.split("/");
        //打印数组
        System.out.println(Arrays.toString(result1));//[, aaa, bbb, ccc]
        System.out.println(Arrays.toString(result2));//[name, yan, age]
    }

}
