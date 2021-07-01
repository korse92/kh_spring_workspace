package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kh.spring.member.model.vo.Member;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//언제 어디서는 현재 request객체를 가져오는 방법
//		HttpServletRequest req = 
//				((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
//				.getRequest();
				
		//로그인 검사
		HttpSession session = request.getSession();
		Member loginMember = (Member)session.getAttribute("loginMember");
		
		if(loginMember == null) {
			//RedirectAttributes 내부에서 사용하는 FlashMap에 사용자 피드백 저장
			FlashMap flashMap = new FlashMap();
			flashMap.put("msg", "로그인 후 이용하세요.");
			//RequestContextUtils : DispatcherServlet에서 설정한 request별 상태에 쉽게 액세스할 수 있는 유틸리티 클래스
			FlashMapManager manager = RequestContextUtils.getFlashMapManager(request);
			//FlashMapManager를 통해 RedirectAttributes의 FlashMap에 접근 가능
			manager.saveOutputFlashMap(flashMap, request, response);
			
			response.sendRedirect(request.getContextPath());
			return false;
		}		
		
		return super.preHandle(request, response, handler);
	}

}
