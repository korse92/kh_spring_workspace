package com.kh.security.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {

	@Autowired
	private SqlSessionTemplate session;

	@Override
	public Member selectOneMember(String id) {
		return session.selectOne("security.selectOneMember", id);
	}
	
	
}
