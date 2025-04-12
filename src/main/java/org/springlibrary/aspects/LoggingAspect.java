package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* org.springlibrary.services..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        System.out.println("Calling method: " + methodName + " with args: " + Arrays.toString(args));
        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println("Method succeeded with result: " + result);
        } catch (Throwable e) {
            System.out.println("Method failed with exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

}
