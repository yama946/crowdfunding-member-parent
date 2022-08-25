package com.yama.crowd.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
public class OtherTestt {
    @Test
    public void UUIDTest(){
        String s1 = UUID.randomUUID().toString();
        log.debug(s1);
    }
}
