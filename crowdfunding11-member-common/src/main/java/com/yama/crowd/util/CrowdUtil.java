package com.yama.crowd.util;


import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 工具类1：
 * 用来判断请求类型是，普通请求，还是json请求
 */
public class CrowdUtil {
    /**
     * 判断是否是ajax请求，是返回true
     * @param request
     * @return
     */
    public static boolean judgeRequestType(HttpServletRequest request){
        String acceptInformation = request.getHeader("Accept");
        String xRequestInformation = request.getHeader("X-Requested-With");
        //普通请求返回true，ajax请求返回false
        return (
                acceptInformation != null
                &&
                acceptInformation.length() > 0
                &&
                acceptInformation.contains("application/json")
                )
                ||
                (
                xRequestInformation != null
                &&
                xRequestInformation.length() > 0
                &&
                xRequestInformation.equals("XMLHttpRequest")
                );
    }

    /**
     * 对密码进行md5加密
     * @param password
     * @return
     */
    public static String md5(String password){
        try {
            //判断密码是否为空
            if(password==null || password.length()<=0){
                throw new RuntimeException("密码输入不合规");
            }
            //创建进行散列加密的对象
            String algorithm  = "md5";
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance(algorithm);
            //进行数据计算处理
            byte[] passwordBytes = password.getBytes();
            messageDigest.update(passwordBytes);
            byte[] magnitude = messageDigest.digest();
            //对加密结果进行处理输出
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, magnitude);
            //将大数转换位大写的字符串进行返回
            String word = bigInteger.toString().toUpperCase();
            return word;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("md5加密操作失败");
        }
    }


}
