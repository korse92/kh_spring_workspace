package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LoggerAspect {
	
	@Pointcut("execution(* com.kh.spring.memo..*(..))")
	public void pointcut() {}
	
	@Before("pointcut()")//Pointcut 메소드를 호출
	public void beforeAdvice(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.debug("[BBBefore] {}", signature);
	}
	
	@Around("pointcut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		
		//joinPoint실행전 처리
		log.debug("[Before] {}", signature);
		
		
		Object obj = joinPoint.proceed(); //주업무 실행
		
		//joinPoint실행후 처리
		log.debug("[After] {}", signature);
		
		return obj;
	}

}
