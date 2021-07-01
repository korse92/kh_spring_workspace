package com.kh.spring.member.model.service;

import java.util.List;
import java.util.Map;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {

	int insertMember(Member member);

	Member selectOneMember(String id);

	int updateMember(Member member);

	List<Member> selectMemberList(Map<String, Object> param);

}
