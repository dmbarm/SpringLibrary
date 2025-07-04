package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CachingAspect {
    private static final Logger logger = LoggerFactory.getLogger(CachingAspect.class);
    private static final Map<String, Object> booksCache = new HashMap<>();

    @Around("execution(* org.springlibrary..*(..)) && !execution(* org.springlibrary.services.io..*(..))")
    public Object checkCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        if (methodSignature.getReturnType().equals(Void.TYPE)) {
            return joinPoint.proceed();
        }

        if (joinPoint.getArgs().length == 0) {
            return joinPoint.proceed();
        }

        String key = methodSignature.toLongString() + ":" + Arrays.toString(joinPoint.getArgs());
        Object result;

        if (booksCache.containsKey(key)) {
            result = booksCache.get(key);
            logger.debug("Retrieved from cache: key={}, result={}", key, result);
        } else {
            result = joinPoint.proceed();
            if (result != null) {
                booksCache.put(key, result);
                logger.debug("Put to cache: key={}, result={}", key, result);
            }
        }

        return result;
    }

}
