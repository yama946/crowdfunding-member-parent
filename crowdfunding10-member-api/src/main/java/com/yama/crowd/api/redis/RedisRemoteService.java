package com.yama.crowd.api.redis;

import com.yama.crowd.util.ResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("crowd-redis")
public interface RedisRemoteService {
    /**
     * 不带超时时间的设置值
     * @param key
     * @param value
     * @return
     */
        @RequestMapping("/set/redis/key/value/remote")
        ResultUtil<String> setRedisKeyValueRemote(
                @RequestParam("key") String key,
                @RequestParam("value") String value);

    /**
     * 带超时时间的设置值
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
        @RequestMapping("/set/redis/key/value/remote/with/timeout")
        ResultUtil<String> setRedisKeyValueRemoteWithTimeout(
                @RequestParam("key") String key,
                @RequestParam("value") String value,
                @RequestParam("time") long time,
                @RequestParam("timeUnit") TimeUnit timeUnit);

    /**
     * 获取值
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/string/value/by/key")
        ResultUtil<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key);

    /**
     * 删除值
     * @param key
     * @return
     */
        @RequestMapping("/remove/redis/key/remote")
        ResultUtil<String> removeRedisKeyRemote(@RequestParam("key") String key);

}
