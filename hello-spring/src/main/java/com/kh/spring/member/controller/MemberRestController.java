package com.kh.spring.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * @RestController : 모든 handler가 @ResponseBody 처리된다.
 * @author korse
 *
 */
@RestController
@Slf4j
@RequestMapping("/member")
public class MemberRestController {
	
	@Autowired
	private MemberService memberService;
	
	/**
	 * 모든 회원을 조회하는 요청
	 * 페이징 처리 필수
	 * cPage = 1(기본값)
	 * numPerPage = 5
	 * 
	 * Mybatis RowBounds사용할 것.
	 * @return
	 */
	@GetMapping("/")
	public List<Member> memberList(@RequestParam(defaultValue = "1") int cPage){		
		int numPerPage = 5;		
		
		int offset = (cPage - 1) * numPerPage;
		int limit = numPerPage;
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("offset", offset);
		param.put("limit", limit);
		
		List<Member> list = memberService.selectMemberList(param);
		
		return list;
	}
	
	/**
	 * @PathVariable 사용
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> member(@PathVariable String id) {
		log.debug("id = {}", id);
		
		Member member = memberService.selectOneMember(id);
		if(member == null)
			return ResponseEntity.notFound().build(); //404
		
		return ResponseEntity.ok().body(member); //200
	}
	
}
