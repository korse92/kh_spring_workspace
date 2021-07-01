package com.kh.spring.tv.model.vo;

import org.springframework.stereotype.Component;

@Component
public class RemoteControl {
	public void changeChannel(int no) {
		System.out.println(no + "번 채널로 변경합니다.");
	}
}
