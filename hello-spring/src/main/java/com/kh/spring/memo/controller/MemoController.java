package com.kh.spring.memo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.memo.model.service.MemoService;
import com.kh.spring.memo.model.vo.Memo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {
	
	@Autowired
	private MemoService memoService;
	
	/**
	 * AOP의 실행 구조
	 * 
	 * MemoController.memo -------------------> MemberService.selectMemoList
	 * 
	 * Spring framework에서 아래와 같은 구조로 실행하도록 바꿔준다.
	 * MemoController.memo ------Proxy객체-----> Target객채(MemberService.selectMemoList)
	 * 
	 * @param mav
	 * @return
	 */
	@GetMapping("/memo.do")
	public ModelAndView memo(ModelAndView mav) {
		//proxy확인
		log.debug("proxy = {}", memoService.getClass());
		//class com.sun.proxy.$Proxy43
		
		try {
			//1. 업무로직
			List<Memo> list = memoService.selectMemoList();
			log.debug("list = {}", list);
			
			//2. jsp위임
			mav.addObject("list", list);
			mav.setViewName("memo/memo");
			
		} catch (Exception e) {
			log.error("메모 조회 오류!", e);
			throw e;
		}
		
		return mav;
	}
	
	@PostMapping("/insertMemo.do")
	public String insertMemo(@ModelAttribute Memo memo, RedirectAttributes redirectAttr) {
		try {
			log.debug("{}", memo);
			//1. 업무로직
			int result = memoService.insertMemo(memo);
			
			//2. 사용자 피드백 및 리다이렉트 준비
			String msg = "메모 저장 성공!";
			redirectAttr.addFlashAttribute("msg", msg);
			
		} catch (Exception e) {
			//1. 에러 로깅
			log.error("메모 등록 오류!", e);
			//2. spring container에게 예외 전달
			throw e;
		}
		
		return "redirect:/memo/memo.do";
	}

}
