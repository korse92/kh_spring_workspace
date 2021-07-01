package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class RuntimeCheckAspect {
	
	@Pointcut("execution(* com.kh.spring.memo.controller..insert*(..))")
	public void pointcut() {}
	
	@Around("pointcut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		//joinPoint 실행 전
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		
		Object obj = joinPoint.proceed();
		
		//joinPoint 실행 후
		stopwatch.stop();
		log.debug("{} 소요시간 {} ms", methodName, stopwatch.getTotalTimeMillis());
		
		return obj;
	}

}
