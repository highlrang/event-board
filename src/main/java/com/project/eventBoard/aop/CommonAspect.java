package com.project.eventBoard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class CommonAspect {

    @Pointcut("execution(* com.project.eventBoard..*(..))")
    public void commonPointCut(){}

    @Pointcut("execution(* com.project.eventBoard..*Repository*.*(..))")
    public void queryTimePointCut(){}

    @Around("commonPointCut()")
    public Object commonAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[추적로그] signature = {}, args = {})", joinPoint.getSignature(), joinPoint.getArgs());

        try {
            Object result = joinPoint.proceed();
            return result;

        } catch (Throwable e) {
            log.error("[에러] " + e.getMessage());
            throw e;
        }
    }

    @Around("queryTimePointCut()")
    public Object queryTimePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            long startTime = System.nanoTime();
            Object result = joinPoint.proceed();
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1000000;
            log.info("[쿼리 수행시간] 메서드 = {}, 실행시간 = {}ms", joinPoint.getSignature().toString(), executionTime);

            return result;

        } catch (Throwable e){
            log.error("[에러] " + e.getMessage());
            throw e;
        }
    }
}
