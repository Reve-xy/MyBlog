package cn.ly.framework.aop;

import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.utils.RedisCache;
import cn.ly.framework.utils.ThreadPoolsUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/4 12:58
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class CacheAspect {

    @Resource
    RedisCache redisCache;
    private static final String PRE = "--- CacheAspect ---";

    /**
     * @param joinPoint
     * @param cacheFind
     * @return Object
     * @date 2023/5/4 13:53
     * @description 缓存
     */
    @Around("within(cn.ly.*.service..*)&&@annotation(cacheFind)")
    public Object doCache(ProceedingJoinPoint joinPoint, CacheFind cacheFind) throws Throwable {
        Object result;
        String key = cacheFind.key();
        // 动态拼接key 获取参数信息
        String args = Arrays.toString(joinPoint.getArgs());
        key += ":" + args;

        String logStr="getRedisCache--->";
        // 缓存是否命中
        Boolean hasKey = redisCache.hasKey(key);
        if (hasKey) {
            log.info(PRE + "Entering " + logStr+ joinPoint.getSignature().getName());
            Object cacheObject = redisCache.getCacheObject(key);
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class returnType = methodSignature.getReturnType();
            result = returnType.cast(cacheObject);
        } else {
            log.info(PRE + "Entering "+ logStr + joinPoint.getSignature().getName());
            result = joinPoint.proceed();
            if (cacheFind.expire() > 0) {
                redisCache.setCacheObject(key, result, cacheFind.expire(), TimeUnit.HOURS);
            } else {
                redisCache.setCacheObject(key, result);
            }
        }
        log.info(PRE + "Exiting " + logStr+joinPoint.getSignature().getName());
        return result;
    }

    @Around("@annotation(cacheDel)")
    public Object delCache(ProceedingJoinPoint joinPoint, CacheDel cacheDel) throws Throwable {
        log.info(PRE + "Entering delCache --->" + joinPoint.getSignature().getName());
        Object result;
        String redisPrefix = cacheDel.redisPrefix();
        String fieldName = cacheDel.fieldName();
        String[] delArray = cacheDel.delArray();
        if(Objects.nonNull(delArray)){
            // 遍历需要删除的缓存Key，逐个进行删除
            for (String key : delArray) {
                String cacheKey = parseCacheKey(key, joinPoint.getArgs());
                redisCache.deleteObject(cacheKey);
            }
        }
        // 不存在后缀，根据前缀删除缓存
        if (!StringUtils.hasText(fieldName)&&Objects.nonNull(redisPrefix)) {
            Collection<String> keys = redisCache.keys(redisPrefix + "*");
            redisCache.deleteObject(keys);
        }
        result = joinPoint.proceed();


        // 延时删除
        ThreadPoolsUtils.exec.execute(() -> {
            try {
                Thread.sleep(1000);
                log.info("开始执行延迟双删的线程--->{}",Thread.currentThread().getName());
                Collection<String> keys = redisCache.keys(redisPrefix + "*");
                redisCache.deleteObject(keys);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info(PRE + "Exiting delCache --->" + joinPoint.getSignature().getName());
        return result;
    }

    /**
     * 根据方法参数解析SpEL表达式中的变量，拼接出真实的缓存Key
     */
    private String parseCacheKey(String key, Object[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable("arg" + i, args[i]);
        }
        Expression expression = parser.parseExpression(key);
        return expression.getValue(context, String.class);
    }
}
