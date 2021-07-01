package com.kh.security.model.dao;

import com.kh.spring.member.model.vo.Member;

public interface UserDetailsDao {

	Member selectOneMember(String id);

}
