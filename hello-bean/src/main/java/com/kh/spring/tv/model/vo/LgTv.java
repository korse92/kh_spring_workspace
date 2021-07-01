package com.kh.spring.tv.model.vo;

import org.springframework.stereotype.Component;

@Component
public class LgTv implements Tv {
	
	public LgTv() {
		System.out.println("LgTv객체 생성!");
	}

	public void powerOn() {
		System.out.println("LgTv의 전원을 켰습니다.");
	}

}
