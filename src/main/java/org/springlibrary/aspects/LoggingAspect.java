package org.springlibrary.aspects;

import lombok.SneakyThrows;
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
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @SneakyThrows
    @Around("execution(* org.springlibrary.services..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        if (logger.isDebugEnabled()) {
            logger.debug("Calling method: {} with args: {}", methodName, Arrays.toString(args));
        }

        try {
            Object result = joinPoint.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug("Method {} succeeded with result: {}", methodName, result);
            }
            return result;
        } catch (Throwable e) {
            logger.error("Method {} failed with exception", methodName, e);
            throw e;
        }
    }
}
