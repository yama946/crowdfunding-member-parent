package com.yama.crowd.test;

import com.yama.crowd.redis.CrowdMemberRedis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CrowdMemberRedis.class})
public class RedisTest {
    //如果不配置，springboot会自动生成导入redistemplate
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisOpreation(){
        //获取操作字符串的对象
        ValueOperations<String, String> strOps = redisTemplate.opsForValue();
        strOps.set("username","tom");
        //带延时时间的设置
        strOps.set("age","23",60, TimeUnit.SECONDS);
    }
}
