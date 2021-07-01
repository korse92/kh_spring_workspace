package com.kh.spring.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StompController {
	
	/**
	 * @MessageMapping prefix제외하고 이후값을 작성할 것
	 * @SendTo SimpleBroker에 등록된 url로 보낼 것
	 * @param msg
	 * @return
	 */
	@MessageMapping("/a")
	@SendTo("/appppp")
	public String app(String msg) {
		//db저장 로직등이 가능
		log.info("/app 요청 : {}", msg);		
		return msg;
	}

}
