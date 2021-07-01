package com.kh.spring.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.service.DemoService;
import com.kh.spring.demo.model.vo.Dev;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * #컨트롤러클래스 메소드(핸들러)가 가질수 있는 파라미터 
 * 
 * HttpServletRequest
 * HttpServletResponse
 * HttpSession
 * java.util.Locale : 요청에 대한 Locale
 * InputStream/Reader : 요청에 대한 입력스트림
 * OutputStream/Writer : 응답에 대한 출력스트림. ServletOutputStream, PrintWriter * 
 * 
 * @PathVariable: 요청url중 일부를 매개변수로 취할 수 있다.
 * @RequestParam
 * @RequestHeader
 * @CookieValue
 * @RequestBody
 * 
 * 뷰에 전달할 모델 데이터 설정
 * 	ModelAndView
 * 	Model
 * 	ModelMap 
 * 	@ModelAttribute : model속성에 대한 getter
 * 	@SessionAttribute : session속성에 대한 getter
 * 	SessionStatus: @SessionAttribute로 등록된 속성에 대하여 사용완료(complete)처리
 * 	Command객체 : http요청 파라미터를 커맨드객체에 저장한 VO객체
 * 	Error, BindingResult : Command객체에 저장결과, Command객체 바로 다음위치시킬것.
 * 
 * 기타
 * 	MultipartFile : 업로드파일 처리 인터페이스. CommonsMultipartFile
 * 	RedirectAttributes : DML처리후 요청주소 변경을 위한 redirect를 지원
 */
@Slf4j //로깅관련 어노테이션
@Controller
@RequestMapping("/demo")//클래스 레벨 RequestMapping
public class DemoController {
	
	//@Slf4j에 의해 생성되는 코드
	//Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DemoService demoService; //MVC 패턴 클래스들은 인터페이스 타입으로 제어
	
	/**
	 * 사용자 요청 url에 따라 호출되는 메소드
	 * @return
	 */
	@RequestMapping("/devForm.do")//메소드 레벨 RequestMapping
	public String devForm() {
		return "demo/devForm";
	}
	
//	@RequestMapping("/demo/dev1.do") //GET POST처리
	@RequestMapping(value = "/dev1.do", method = RequestMethod.POST)
	public String dev1(HttpServletRequest request, HttpServletResponse response, Model model) {
		String name = request.getParameter("name");
		int career = Integer.valueOf(request.getParameter("career"));
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String[] lang = request.getParameterValues("lang");
		int test;
		
		Dev dev = new Dev(0, name, career, email, gender, lang);
//		System.out.println(dev);
		//INFO : 호출된 클래스 이름 - "로그내용" : logger가 어느 클래스에서 호출됐는지도 알려준다.
		log.info("dev = {}", dev);//중괄호부분의 객체 정보를 파라미터로 줌
		
		//request.setAttribute("dev", dev);
		model.addAttribute("dev", dev);// model객체를 통해 jsp에 객체, 값 등을 전달.
		
		return "demo/devResult";
	}
	
	@RequestMapping(value = "/dev2.do", method = RequestMethod.POST)
	public String dev2(
				@RequestParam(value = "name") String name,
				@RequestParam(value = "career") int career,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "gender", defaultValue = "M") String gender,
				@RequestParam(value = "lang", required = false) String[] lang,
				Model model
			) {
		Dev dev = new Dev(0, name, career, email, gender, lang);
		log.info("{}", dev);		
		
		model.addAttribute("dev", dev);
		return "demo/devResult";
	}
	
	/**
	 * 커맨드 객체 : 사용자 입력값과 일치하는 필드에 값대입
	 * (요청 파라미터의 이름과 일치하는 핸들러 파라미터 객체 필드에 자동으로 맵핑해준다.)
	 * 커맨드객체는 자동으로 model객체의 속성으로 지정된다.	 
	 * 
	 * @param dev
	 * @return
	 */
	//@RequestMapping(value = "/dev3.do", method = RequestMethod.POST)
	@PostMapping("/dev3.do")//POST요청으로 맵핑
	public String dev3(@ModelAttribute("ddddddev") Dev dev) {
		//커맨드 객체는 Model객체의 속성으로 자동 지정되므로 포워딩한 jsp에서 바로 사용가능하다.
		log.info("{}", dev);
		return "demo/devResult";
	}
	
	@PostMapping("/insertDev.do")
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		//RedirectAttributes는 리다이렉트 시의 요청객체를 가리킨다.
		log.info("{}", dev);
		//1. 업무로직
		int result = demoService.insertDev(dev);
		//2. 사용자 피드백 및 리다이렉트(DML인 경우만)
		String msg = result > 0 ? "Dev등록 성공!" : "Dev등록 실패!";
		log.info("처리결과 : {}", msg);
		//리다이렉트 후 사용자 피드백 전달하기
		redirectAttr.addFlashAttribute("msg", msg);
		
		return "redirect:/"; //웹앱 루트로 리다이렉트
	}
	
	@GetMapping("/devList.do")
	public String devList(Model model) {
		//1. 업무로직
		List<Dev> list = demoService.selectDevList();
		log.info("list = {}", list);
		
		//2. jsp 위임
		model.addAttribute("list", list);
		
		return "demo/devList";
	}
	
	@GetMapping("/updateDev.do")
	public String updateDev(@RequestParam(required = true) int no, Model model) {
		//0. 사용자 입력값 확인
		log.info("no = {}", no);
		
		//1. 업무로직
		Dev dev = demoService.selectDevOne(no);
		log.info("{}", dev);
		
		//2. jsp위임
		model.addAttribute("dev", dev);
		
		return "demo/devUpdateForm";
	}
	
	@PostMapping("/updateDev.do")
	public String updateDev(Dev dev, RedirectAttributes redirectAttr) {		
		//1. 업무로직
		int result = demoService.updateDev(dev);
		log.info("{}", dev);
		
		//2. 리다이렉트 및 사용자 피드백
		String msg = result > 0 ? "Dev 수정 성공!" : "Dev 수정 실패!";
		redirectAttr.addFlashAttribute("msg", msg);
		
		return "redirect:/demo/devList.do";
	}
	
	@PostMapping("/deleteDev.do")
	public String deleteDev(@RequestParam int no, RedirectAttributes redirectAttr) {
		//@RequestParam으로 입력값에 어노테이션을 지정하면 서버단에서 부정한 방법의 POST요청을 막을 수 있다.(ex. no값이 없는 경우)
		int result = demoService.deleteDev(no);
		
		//2. 리다이렉트 및 사용자 피드백
		String msg = result > 0 ? "Dev 삭제 성공!" : "Dev 삭제 실패!";
		redirectAttr.addFlashAttribute("msg", msg);
		
		return "redirect:/demo/devList.do";		
	}
}
