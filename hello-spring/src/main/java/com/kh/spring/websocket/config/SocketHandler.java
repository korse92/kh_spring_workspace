package com.kh.spring.websocket.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {
	
	/**
	 * 접속한 사용자를 관리
	 * 
	 * CopyOnWriteArrayList - ArrayList를 확장(상속)해서 동기화처리 지원.(멀티스레드 환경에서 사용하기 용이)
	 */
	private List<WebSocketSession> list = new CopyOnWriteArrayList<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		list.add(session);
		log.info("새로운 새션 : {}", session);
		log.info("현재 새션 수(+) : {}", list.size());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String id = session.getId();
		String payload = message.getPayload(); //메시지의 내용을 가져옴
		log.info("메세지 - id[{}] : {}", id, payload);
		
		message = messageToJson(id, payload);
		
		for(WebSocketSession sess : list) {
			sess.sendMessage(message);
		}
	}

	private TextMessage messageToJson(String id, String payload) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("message", payload);
		map.put("type", "message");
		map.put("time", System.currentTimeMillis());
		
		return new TextMessage(new Gson().toJson(map));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		list.remove(session);
		log.info("현재 새션 수(-) : {}", list.size());
	}
	
}
