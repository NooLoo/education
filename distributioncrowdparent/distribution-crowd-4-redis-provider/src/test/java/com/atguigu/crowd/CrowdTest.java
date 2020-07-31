package com.atguigu.crowd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSaveValueToRedis(){
        //获取Redis操作器
        ValueOperations<Object, Object> operator = redisTemplate.opsForValue();

        //设置值
        //operator.set("keyone","valueone");

        Object keyone = operator.get("keyone");
        System.out.println(keyone);
    }
}
