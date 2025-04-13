package org.springlibrary.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springlibrary.models.Book;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CachingAspect {
    private static final Map<String, Book> booksCache = new HashMap<>();

    @Around("execution(org.springlibrary.models.Book org.springlibrary.services.LibraryService.findByIdOrTitle(String))")
    public Object checkBooksCache(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String input = (String) args[0];
        Book book;
        if (booksCache.containsKey(input)) {
            book = booksCache.get(input);
        }
        else {
            Object result = joinPoint.proceed();
            book = (Book) result;
            booksCache.put(input, (Book) result);
        }

        return book;
    }

}
