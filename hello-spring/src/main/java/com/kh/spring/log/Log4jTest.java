package com.kh.spring.log;

import org.apache.log4j.Logger;

/**
 * logging 구현체인 log4j를 직접 사용한다.
 * 
 * level을 통해 logging모드를 변경할 수 있다. (log4j.xml에서 logger별로 변경)
 * 
 * logging level
 * -----------------
 * fatal : 아주 치명적인 오류 발생시
 * error : 요청 처리중 예외가 발생한 경우
 * warn : 현재 실행에는 문제가 없지만, 잠재적 오류인 경우
 * info : 상태변경 등 정보성 메세지인 경우
 * debug : 개발시 디버깅 용도 출력인 경우
 * trace : 디버깅을 상세히 구분지어야 하는 경우
 * -----------------
 *
 */
public class Log4jTest {
	
	private Logger log = Logger.getLogger(Log4jTest.class);
	
	public static void main(String[] args) {
		new Log4jTest().logTest();
	}

	private void logTest() {
		log.fatal("fatal");
		log.error("error");
		log.warn("warn");
		log.info("info");
		log.debug("debug");
		log.trace("trace");
	}

}
