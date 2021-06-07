package com.baizhi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
@Aspect
public class CacheAspect {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Around("@annotation(com.baizhi.annotation.AddCache)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint){
        //设置序列化方式  解决key乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //字符串拼接
        StringBuilder sb = new StringBuilder();

        //缓存的逻辑处理   Redis
        //key:类的全限定名+方法名+实参    value:数据
        //key:String value:Hash<key,value>
        //KEY  key,value
        //KEY:类的全限定名  key:方法名+实参,value:数据
        //类的全限定名  com.baizhi.serviceimpl.UserServiceImpl
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //获取方法名   queryAllPage
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);  //拼接方法名

        //获取参数数组   1  5 | 10
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            //System.out.println("获取参数: "+arg);
            sb.append(arg);  //拼接参数
        }

        //获取拼接好的key
        String key = sb.toString();

        //获取String类型的操作
        HashOperations hashOps = redisTemplate.opsForHash();

        //1.判断该缓存是否存在   获取redis中数据的key
        //2.先判断有没有缓存 redis中取缓存
        Boolean aBoolean = hashOps.hasKey(className, key);

        Object result =null;

        if(aBoolean){
            //3-1.有 取出缓存返回结果
            result = hashOps.get(className,key);
        }else{
            //3-2.没有
            // 放行目标方法,查询数据库
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            //将返回结果加入缓存
            hashOps.put(className,key,result);
        }
        return result;
    }

    @AfterReturning("@annotation(com.baizhi.annotation.DelCache)")
    public void delCache(JoinPoint joinPoint){

        System.out.println("----------进入返回通知----------");

        String className = joinPoint.getTarget().getClass().getName();

        //com.baizhi.serviceimpl.UserServiceImpl queryAllPage 15
        /**
         com.baizhi.serviceimpl.UserServiceImpl
         queryAllPage15:数据
         queryAllPage110:数据
         * */
        //清除缓存
        stringRedisTemplate.delete(className);
    }


}
