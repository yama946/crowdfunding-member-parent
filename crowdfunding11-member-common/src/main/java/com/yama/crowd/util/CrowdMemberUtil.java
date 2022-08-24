package com.yama.crowd.util;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.yama.crowd.aliyun.api.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具类1：
 * 用来判断请求类型是，普通请求，还是json请求
 */
public class CrowdMemberUtil {

    static Logger logger = LoggerFactory.getLogger(CrowdMemberUtil.class);
    /**
     * 发送短信的工具类
     * @param host  短信api地址
     * @param path  短线api地址uri
     * @param method   请求方式
     * @param appcode   申请短信接口的验证方式
     * @param phone     发送短信的验证码
     * @param verityCode  生成的验证码
     * @param smsSignId 短信变量
     * @param templateId    短信模板
     * @return
     */
    public static ResultUtil<HttpResponse> sendCodeByShortMessage(
            String host,
            String path,
            String method,
            String appcode,
            String phone,
            String verityCode,
            String smsSignId,
            String templateId
    ){

        HttpResponse response=null;
        //1.短信api接口地址
//        String host = "https://gyytz.market.alicloudapi.com";
        //2.短信服务api地址uri
//        String path = "/sms/smsSend";
//        String method = "POST";
//        String appcode = "40394e3b27af4ca3867e3a115b6478e7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        //暂时生成一个六位数验证码不存放在redis中永久有效
        int code = (int)(Math.random()*1000000);
//        String verityCode = "**code**:"+code+",**minute**:2";
        querys.put("param",verityCode);
        querys.put("smsSignId", smsSignId);//2e65b1bb3d054466b82f0c9d125465e2
        querys.put("templateId", templateId);//908e94ccf08b4476ba6c876d13f084ad
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
//            System.out.println();
            logger.debug("验证码发送成功");
            //输出结果为：{"msg":"成功","code":"0"}
            return ResultUtil.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultUtil.fail();
        }
    }


}
