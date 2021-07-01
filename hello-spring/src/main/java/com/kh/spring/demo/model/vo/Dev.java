package com.kh.spring.demo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//lombok에서 컴파일 시 자동으로 생성자, getter/setter, toString 등의 메소드를 자동으로 생성해준다.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Dev {
	
	private int no;
	private String name;
	private int career;
	private String email;
	private String gender;
	private String[] lang;
}
