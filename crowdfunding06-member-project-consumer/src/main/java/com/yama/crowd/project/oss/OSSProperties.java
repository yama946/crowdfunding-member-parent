package com.yama.crowd.project.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OSSProperties {
    //外网上传地址
    private String endpoint;

    //子账户id
    private String accessKeyId;

    //子账户密码
    private String accessKeySecret;

    //账户名
    private String bucketName;

    //bucket中建立的文件夹
    private String objectName ;

    //下载文件的地址
    private String bucketDomain;
}
