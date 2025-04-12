package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* org.springlibrary.services..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.debug("Calling method: {} with args: {}", methodName, Arrays.toString(args));
        Object result;
        try {
            result = joinPoint.proceed();
            log.debug("Method succeeded with result: {}",result);
        } catch (Throwable e) {
            log.error("Method failed with exception: {}", e.getMessage());
            throw e;
        }

        return result;
    }
}
