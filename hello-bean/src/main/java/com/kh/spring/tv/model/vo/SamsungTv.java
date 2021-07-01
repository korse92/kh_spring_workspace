package com.kh.spring.tv.model.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SamsungTv implements Tv {
	
	//RemoteControl빈을 생성해서 대신 setting 요청
	//@Autowired : 해당 타입의 bean을 찾아 의존객체 주입(Dependency Injection)
	//타입 -> bean id 순으로 container에서 조회
	//@Autowired
	private RemoteControl remocon;
	
	//@Autowired
	public void setRemocon(RemoteControl remocon) {
		this.remocon = remocon;
	}
	
	public SamsungTv() {
		System.out.println("SamsungTv객체 생성!");
	}
	
	@Autowired
	public SamsungTv(RemoteControl remocon) {
		System.out.println("SamsungTv(RemoteControl) 객체 생성!");
		this.remocon = remocon;
	}

	public void powerOn() {
		System.out.println("SamsungTv의 전원을 켰습니다.");
	}
	
	public void changeChannel(int no) {
		System.out.println(remocon);
		this.remocon.changeChannel(no);
	}

}
