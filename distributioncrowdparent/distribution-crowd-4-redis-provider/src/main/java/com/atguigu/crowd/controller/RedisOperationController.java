package com.atguigu.crowd.controller;

import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisOperationController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 将字符串类型的键值对保存到Redis时调用的远程方法
     * @param normalKey  所要保持的数据对应的key值
     * @param normalValue   所要保持的数据对应的value值
     * @param timeoutMinute	超时时间（单位：分钟）
     * 		-1表示无过期时间
     * 		正数表示过期时间分钟数
     * 		0和null值不接受
     */
    @RequestMapping("/save/normal/string/key/value")
    ResultEntity<String> saveNormalStringKeyValue(
            @RequestParam("normalKey") String normalKey,
            @RequestParam("normalValue") String normalValue,
            @RequestParam("timeoutMinute") Integer timeoutMinute){

        //对输入数据进行验证
        if(!CrowdUtils.strEffectiveCheck(normalKey) || !CrowdUtils.strEffectiveCheck(normalValue)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_REDIS_KEY_OR_VALUE_INVALID);
        }

        //判断timeoutMinute是否为无效值
        if(timeoutMinute == null || timeoutMinute == 0){
            return ResultEntity.failed(CrowdConstant.MESSAGE_REDIS_KEY_TIME_OUT_INVALID);
        }
        //获取字符串操作对象
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        //按照未设置过期时间保存
        if(timeoutMinute == -1){
            operations.set(normalKey,normalValue);
            return ResultEntity.successNoData();
        }

        //按照设置过期时间保存
        operations.set(normalKey,normalValue,timeoutMinute, TimeUnit.MINUTES);
        return ResultEntity.successNoData();
    }

    /**
     * 根据key查询对应value时调用的远程方法
     * @param normalKey  所要查询的数据对应的key值
     */
    @RequestMapping("/retrieve/string/value/by/string/key")
    ResultEntity<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey){

        //对输入数据进行验证
        if(!CrowdUtils.strEffectiveCheck(normalKey)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_REDIS_KEY_OR_VALUE_INVALID);
        }

        String s = redisTemplate.opsForValue().get(normalKey);
        return ResultEntity.successWithData(s);
    }

    /**
     * 根据key删除对应value时调用的远程方法
     * @param key  所要删除数据对应的key值
     * @return
     */
    @RequestMapping("/redis/remove/by/key")
    ResultEntity<String> removeByKey(@RequestParam("key") String key){

        //对输入数据进行验证
        if(!CrowdUtils.strEffectiveCheck(key)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_REDIS_KEY_OR_VALUE_INVALID);
        }
        redisTemplate.delete(key);

        return ResultEntity.successNoData();
    }
}
