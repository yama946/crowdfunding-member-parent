package com.yama.crowd.project.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.netflix.discovery.converters.Auto;
import com.yama.crowd.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class OSSUploadUtile {

    /**
     * 专门负责上传文件到OSS 服务器的工具方法
     * @param endpoint OSS 参数
     * @param accessKeyId OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream 要上传的文件的输入流
     * @param bucketName OSS 参数
     * @param bucketDomain OSS 参数
     * @param originalName 要上传的文件的原始文件名
     * @param objectName oss中要上传文件夹
     * @return 包含上传结果以及上传的文件在OSS 上的访问路径
     */
    public static ResultUtil<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName,
            String objectName) {
        // 创建OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String fileName = objectName+"/"+folderName + "/" + fileMainName + extensionName;
        try {
            // 调用OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName,inputStream);
            log.debug("上传后的返回值:{}",putObjectResult);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if(responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + fileName;
                // 当前方法返回成功
                return ResultUtil.ok(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultUtil.fail(" 当前响应状态码="+statusCode+" 错误消息="+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultUtil.fail(e.getMessage());
        } finally {
            if(ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }


}
