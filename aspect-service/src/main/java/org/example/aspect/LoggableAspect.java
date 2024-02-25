package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Order(2)
@Component
public class LoggableAspect {

    @Pointcut("@annotation(org.example.aspect.Loggable)")
    public void methodsAnnotatedWith() {
    }

    @Around("methodsAnnotatedWith()")
    public Object loggableAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Level level = extractLevel(joinPoint);

        log.atLevel(level).log("target = {}", joinPoint.getTarget().getClass());
        log.atLevel(level).log("method = {}", joinPoint.getSignature().getName());
        log.atLevel(level).log("args = {}", Arrays.toString(joinPoint.getArgs()));

        try {
            Object returnValue = joinPoint.proceed();
            log.atLevel(level).log("result = {}", returnValue);
            return returnValue;
        } catch (Throwable e) {
            log.atLevel(level).log("Exception = [{}, {}]", e.getClass(), e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Метод " + joinPoint.getSignature() + " выполнялся " +
                    (endTime - startTime) + " миллисекунд.");
        }
    }

    private Level extractLevel(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Loggable annotation = signature.getMethod().getAnnotation(Loggable.class);
        if (annotation != null) {
            return annotation.level();
        }

        return joinPoint.getTarget().getClass().getAnnotation(Loggable.class).level();
    }
}