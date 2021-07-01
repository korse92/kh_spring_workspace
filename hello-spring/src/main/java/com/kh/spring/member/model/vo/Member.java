package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@EqualsAndHashCode
public class Member {
	
	private String id;
	private String password;
	private String name;
	private String gender;
	//java.sql.Date는 시분초정보는 누락된다. 시분초 정보가 필요할 경우 java.util.Date 사용
	//스프링에서는 java.util.Date 사용 가능
	private Date birthday;
	private String email;
	private String phone;
	private String address;
	private String[] hobby;
	private Date enrollDate;
	private boolean enabled;
	
}
