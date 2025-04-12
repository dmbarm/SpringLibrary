package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* org.springlibrary.services..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        String currentTime = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) + "] ";
        System.out.println(currentTime + "Calling method: " + methodName + " with args: " + Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println(currentTime + "Method succeeded with result: " + result);
        } catch (Throwable e) {
            System.out.println(currentTime + "Method failed with exception: " + e.getMessage());
            throw e;
        }

        return result;
    }
}
