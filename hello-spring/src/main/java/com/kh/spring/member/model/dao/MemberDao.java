package com.kh.spring.member.model.dao;

import java.util.List;
import java.util.Map;

import com.kh.spring.member.model.vo.Member;

public interface MemberDao {

	int insertMember(Member member);

	Member selectOneMember(String id);

	int updateMember(Member member);

	List<Member> selectMemberList(Map<String, Object> param);

}
