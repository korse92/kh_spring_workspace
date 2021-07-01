package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.gson.Gson;
import com.kh.spring.member.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * Model
 * - view단에서 처리할 데이터 저장소. Map객체
 * 1. Model<<interface>>
 * 		- viewName 리턴
 * 		- addAttribute(k, v)
 * 2. ModelMap
 * 		- viewName 리턴
 * 		- addAttribute(k, v)
 * 3. ModelAndView
 * 		- viewName(jsp위치, redirect location) 포함, ModelAndView객체 리턴
 * 		- addObject(k, v)
 * 		- RedirectAttributes 객체와 함께 사용하지 말것. (충돌 일어남)
 * 
 * @ModelAttribute
 * 1. 메소드 레벨
 * 		- 해당메소드의 리턴값을 model에 저장해서 모든 요청에 사용.
 * 2. 메소드 매개변수에 지정
 * 		- model에 저장된 동일한 이름의 속성이 있는 경우 getter로 사용
 * 		- 해당 매개변수를 model속성으로 저장
 * 			- 커맨드객체같이 모델에 Attribute로 저장된 값만 @ModelAttribute(속성명)으로 지정하여 가져올 수 있다.
 * 			- 단순 사용자 입력값은 Model에 저장되지 않으니까 @RequestParam 으로 처리 할 것(받아올 것). (@ModelAttribute 로는 받아오지 못한다)
 *
 */
@Slf4j
@Controller
@RequestMapping("/member")
@SessionAttributes(value = {"loginMember", "anotherValue"})//여러개 지정할 경우 String 배열로 저장
public class MemberController {
	
	//lombok에 의해 자동으로 생성되는 로그관련 객체 생성 코드
	//private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MemberController.class);  
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@ModelAttribute("common")
	public Map<String, Object> common() {
		log.debug("@ModelAttribute - common 실행!");
		Map<String, Object> map = new HashMap<>();
		map.put("adminEmail", "admin@email.com");
		
		return map;
	}
	
	@GetMapping("/memberEnroll.do")
	public void memberEnroll() {
		//ViewTranslator에 의해서 요청 url에서 view단 jsp주소를 추론한다.
		
//		return "member/memberEnroll";
	}
	
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		log.info("member = {}", member);
		
		try {
			//0. 암호화처리
			String rawPassword = member.getPassword();
			//랜덤 salt값 사용하여 암호화 하기 때문에 매번 다른 암호화값이 생성된다.(같은값을 암호화한다고 하더라도)
			String encodeedPassword = bCryptPasswordEncoder.encode(rawPassword);
			log.info("rawPassword = {}", rawPassword);
			log.info("encodeedPassword = {}", encodeedPassword);
			member.setPassword(encodeedPassword);
			
			//1. 비지니스 로직		
			int result = memberService.insertMember(member);
			String msg = result > 0 ? "회원가입 성공!" : "회원가입 실패!";
			
			//2. 사용자피드백 준비 및 리다이렉트
			redirectAttr.addFlashAttribute("msg", msg);
			
		} catch (Exception e) {
			//1. 로깅작업
			log.error(e.getMessage(), e);
			//2. 다시 spring container에게 던질 것.
			throw e;
		}
		
		return "redirect:/";
	}
	
	/**
	 * 커맨드객체 이용시 사용자 입력값(String)을 특정필드타입으로 대입할 editor객체를 설정
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//Member.birthday:java.sql.Date 타입처리
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//date 입력값을 입력하지않으면 ''으로 넘어와 null로 변환처리가 자동으로 안되어서 커스텀에디터 생성
		//커스텀에디터 생성 : allowEmpty - true(빈문자열을 null로 변환처리 허용)
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		binder.registerCustomEditor(java.sql.Date.class, editor);
		
	}
	
	@GetMapping("/login.do")
	public ModelAndView login(ModelAndView mav) {
		// /WEB-INF/views/member/login.jsp에 위임하게됨(void일 경우 return 생략해도 ViewTranslator에 의해서 자동으로 위임)
		
		mav.addObject("test", "hello world");
		mav.setViewName("member/login");
		
		return mav;
	}
	
	@PostMapping("/login.do")
	public ModelAndView login(
			@RequestParam String id,
			@RequestParam String password,
			ModelAndView mav,
			HttpServletRequest request) {
		
		try {			
			log.info("id = {}, password = {}", id, password);
			//1. 업무로직 : 해당 id의 member조회
			Member member = memberService.selectOneMember(id);
			log.info("member = {}", member);
//			log.info("encodeedPasswod = {}", bCryptPasswordEncoder.encode(password));
			
			//2. 로그인 여부처리
			/*
			 * bCryptPasswordEncoder.matches(rawpassword, encodedPassword)
			 * rawPassword를 encodePassword의 salt값을 이용해 hashvalue를 만든다음
			 * 변환된 rawpassword의 hashValue와 encodeedPassword의 hashvalue를 비교한다.
			 */
			if(member != null && bCryptPasswordEncoder.matches(password, member.getPassword())) {
				//로그인 성공
				//기본값으로 request scope속성에 저장됨.
				//클래스 레벨(클래스 선언부)에 @SessionAttributes("loginMember") 지정하면, session scope에 저장
				mav.addObject("loginMember", member);
			} else {
				log.info("로그인실패!");
				//로그인 실패
				//redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				
				//ModelAndView와 RedirectAttribute는 동시에 사용하면 충돌이 일어나 에러가 발생한다.
				//그렇기때문에 flashMap에 따로 접근하여 처리한다.
				FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
				flashMap.put("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			//3. 사용자피드백 및 리다이렉트
			
		} catch (Exception e) {
			//1. logging
			log.error(e.getMessage(), e);
			//2. spring container에게 예외를 다시 던져서 error페이지로 이동시킨다.
			throw e;
		}
		
		mav.setViewName("redirect:/");		
		return mav;
	}
	
	/**
	 * @SessionAttributes 를 통한 로그인은
	 * SessionStatus객체를 통해서 사용완료처리함으로 로그아웃한다.
	 * 
	 * @return
	 */
	@GetMapping("/logout.do")
	public String logout(SessionStatus sessionStatus) {
		if(!sessionStatus.isComplete())
			sessionStatus.setComplete();		
		return "redirect:/";
	}
	
	@GetMapping("/memberDetail.do")
	public ModelAndView memberDetail(@ModelAttribute("loginMember") Member loginMember, ModelAndView mav) {
		log.info("loginMember = {}", loginMember);
		
		String[] hobbyArr = loginMember.getHobby();
		List<String> hobbyList = hobbyArr != null ? Arrays.asList(hobbyArr) : null;
		
		mav.addObject("hobbyList", hobbyList);
		mav.setViewName("member/memberDetail");
		return mav;
	}
	
	/**
	 * id=hongd&name=홍길동&gender=M&hobby=영화&hobby=운동
	 * 
	 * 각각 처리하려고 할때 -> @RequestParam
	 * 한번에 커맨드객체로 처리할 경우(생략가능) -> @ModelAttribute
	 * 
	 * 모든 command객체는 model에 자동 등록됨.
	 */
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(
			@ModelAttribute Member member,
			RedirectAttributes redirectAttr,
			@ModelAttribute("loginMember") Member loginMember) {
		
		try {
			
			log.debug("param = {}", member);
			log.debug("loginMember = {}", loginMember);
			
			//1. 업무로직
			int result = memberService.updateMember(member);
			
			//2. 사용자피드백 준비 및 리다이렉트
			String msg = "회원정보 수정 성공!";
			
			if(result > 0) {
				//회원정보 수정 성공시 session객체 갱신
				loginMember = memberService.selectOneMember(member.getId());	
//				Member updateMember = memberService.selectOneMember(member.getId());
//				model.addAttribute("loginMember", updateMember);
				
			} else {
				throw new MemberException("해당회원이 존재하지 않습니다 : " + member.getId());
			}			
			redirectAttr.addFlashAttribute("msg", msg);			
			
		} catch (Exception e) {
			//1. 에러로깅
			log.error("회원정보 수정 실패", e);			
			//2. 컨테이너에게 예외 던져줌
			throw e;
		}
		
		return "redirect:/";
	}
	
	/**
	 * Spring Ajax
	 * 1. BeanNameViewResolver : jsonView라는 bean을 이용해서 json 출력
	 * 2. 응답스트림 직접 작성 : 응답출력스트림에 json문자열 출력
	 * 3. @ResponseBody : handler의 리턴객체를 messageConverter빈에 의해 json 변환 출력
	 * 4. ResponseEntity
	 * 
	 * @param id
	 * @return
	 */
	//1. BeanNameViewResolver로 처리
	@GetMapping("/checkIdDuplicate1.do")
	public String checkIdDuplicate1(@RequestParam String id, Model model) {
		log.debug("id = {}", id);
		//1.업무로직
		Member member = memberService.selectOneMember(id);
		//2.model속성 지정(jsonView에 의해 json문자열로 변환후, 응답출력)
		boolean usable = member == null;
		model.addAttribute("usable", usable);
		model.addAttribute("name", "홍길동");
		model.addAttribute("num", 123);		
		
		return "jsonView";//jsonView bean객체를 반환(BeanNameViewResolver가 먼저 처리함)
	}
	
	//2. 응답스트림 직접 작성
	@GetMapping(value = "/checkIdDuplicate2.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void checkIdDupliate2(@RequestParam String id,
								 HttpServletResponse response,
								 PrintWriter out) {
		
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);
		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "신사임당");
		map.put("serverTime", new Date());
		
		//2. json문자열 응답메세지 출력
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		new Gson().toJson(map, out);		
	}
	
	/**
	 * (RequestMappingHandlerAdapter빈에 등록된)
	 * MessageConverter빈에 의해 json 변환 처리
	 * 
	 * Response Body작성은 @ResponseBody 에 의해 처리
	 * @param id
	 * @return
	 */
	//3. @ResponseBody 
	@GetMapping("/checkIdDuplicate3.do")
	@ResponseBody
	public Map<String, Object> checkIdDuplicate3(@RequestParam String id) {
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);
		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "장보고");
		map.put("serverTime", System.currentTimeMillis()); //new Date().getTime();
		
		return map;
	}
	
	/**
	 * ResponseEntity
	 * 
	 * 1. status code
	 * 2. header값을 자유롭게 설정
	 * 3. @ResponseBody 리턴객체를 json변환 후 응답메세지에 작성
	 * 
	 * <?> : 제네릭에 ?를 주면 모든 클래스 타입을 사용할 수 있다. 
	 * <Object>라고 생각하면 됨
	 * 
	 * @param id
	 * @return
	 */
	//4. ResponseEntity
	@GetMapping("/checkIdDuplicate4.do")
	public ResponseEntity<?> checkIdDuplicate4(@RequestParam String id) {
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);
		
		//2. json변환 객체
		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "이순신");
		map.put("serverTime", System.currentTimeMillis()); //new Date().getTime();
		
		//build 방식
//		return ResponseEntity
//				.ok()
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.body(map);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//new 연산자를 통한 생성자 호출방식
		return new ResponseEntity<>(map, headers, HttpStatus.OK);
	}
	
}
