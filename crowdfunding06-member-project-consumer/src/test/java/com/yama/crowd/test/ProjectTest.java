package com.yama.crowd.test;

import com.netflix.discovery.converters.Auto;
import com.yama.crowd.project.CrowdMemberProject;
import com.yama.crowd.project.oss.OSSProperties;
import com.yama.crowd.project.oss.OSSUploadUtile;
import com.yama.crowd.util.ResultUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrowdMemberProject.class)
public class ProjectTest {
    @Autowired
    private OSSProperties ossProperties;

    @Test
    public void testOss() throws FileNotFoundException {
        File file = new File("D:\\图片\\001HV7sIzy6SCy5SoDMe2&690.jpg");
        String originalName = "001HV7sIzy6SCy5SoDMe2&690.jpg";
        FileInputStream inputStream = new FileInputStream(file);

        ResultUtil<String> stringResultUtil = OSSUploadUtile.uploadFileToOss(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(),
                inputStream, ossProperties.getBucketName(), ossProperties.getBucketDomain(),
                originalName, ossProperties.getObjectName());

        System.out.println(stringResultUtil.getData());
    }
}
