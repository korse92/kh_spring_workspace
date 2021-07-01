package com.kh.spring.member.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository
public class MemberDaoImpl implements MemberDao {
	
	@Autowired
	private SqlSessionTemplate session;

	@Override
	public int insertMember(Member member) {
		return session.insert("member.insertMember", member);
	}

	@Override
	public Member selectOneMember(String id) {
		return session.selectOne("member.selectOneMember", id);
	}

	@Override
	public int updateMember(Member member) {
		return session.update("member.updateMember", member);
	}

	@Override
	public List<Member> selectMemberList(Map<String, Object> param) {
		int offset = (int)param.get("offset");
		int limit = (int)param.get("limit");
		
		return session.selectList("member.selectMemberList", null, new RowBounds(offset, limit));
	}

}
