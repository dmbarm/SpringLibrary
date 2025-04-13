package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CachingAspect {
    private static final Map<String, Object> booksCache = new HashMap<>();

    @Around("execution(* org.springlibrary..*(..))")
    public Object checkBooksCache(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getArgs().length == 0) return joinPoint.proceed();

        String key = joinPoint.getSignature().toString() + ":" + Arrays.toString(joinPoint.getArgs());
        Object result;

        if (booksCache.containsKey(key)) {
            result = booksCache.get(key);
            System.out.println("Retrieved from cache: key=" + key + ", result=" + result);
        }
        else {
            result = joinPoint.proceed();
            if (result != null) {
                booksCache.put(key, result);
                System.out.println("Put to cache: key=" + key + ", result=" + result);
            }
        }

        return result;
    }

}
