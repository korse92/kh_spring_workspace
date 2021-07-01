package com.kh.spring.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.menu.model.exception.MenuNotFoundException;
import com.kh.spring.menu.model.service.MenuService;
import com.kh.spring.menu.model.vo.Menu;

import lombok.extern.slf4j.Slf4j;

/**
 * @RestController 모든 메소드가 @ResponseBody 어노테이션 처리
 */
@RestController
@Slf4j
@CrossOrigin
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/menus")
	public List<Menu> menus(HttpServletResponse response){
		log.debug("/menus 요청");
		List<Menu> list = menuService.selectMenuList();
		log.debug("list = {}", list);
//		response.setHeader("Access-Control-Allow-Origin", "*");// 모든 사이트에 대해 CORS 허용
		return list;
	}
	
//	@CrossOrigin//기본값 : 모든사이트에 대해 CORS 허용
	@GetMapping("/menus/{type}")
	public List<Menu> menusByType(@PathVariable String type){
		log.debug("/menus/{} 요청!", type);
		return menuService.selectMenuListByType(type);
	}
	
	/**
	 * @RequestBody 요청메세지 body에 작성된 json문자열을 command객체로 변환
	 * messageConverter빈에 의해 자동처리됨.
	 * 
	 * @return
	 */
	@PostMapping("/menu")
	public Map<String, String> insertMenu(@RequestBody Menu menu){
		Map<String, String> map = new HashMap<>();
		log.debug("menu = {}", menu);
		try {
			int result = menuService.insertMenu(menu);
			map.put("msg", "메뉴 등록 성공");
		} catch (Exception e) {
			log.error("메뉴 등록 실패!", e);
			throw e;
		}
		
		return map;
	}
	
	@GetMapping("/menu/{id}")
	public ResponseEntity<?> selectOneMenu(@PathVariable int id){
		log.debug("/menu/{}", id);
		Menu menu = menuService.selectOneMenu(id);
		log.debug("menu = {}", menu);
		
		if(menu == null)
			return ResponseEntity.notFound().build();
		
		//MediaType.APPLICATION_JSON_UTF8_VALUE -> deprecated
		return ResponseEntity.ok().body(menu);
	}
	
	@PutMapping("/menu")
	public ResponseEntity<?> updateMenu(@RequestBody Menu menu){
		Map<String, String> map = new HashMap<>();
		log.debug("menu = {}", menu);//RequestBody에서 보낸 객체 확인
		try {
			//업무로직
			int result = menuService.updateMenu(menu);
			
			if(result == 0)
				throw new MenuNotFoundException("id - " + menu.getId());
			
			map.put("msg", "메뉴가 정상적으로 수정되었습니다.");
		} catch (Exception e) {
			log.error("메뉴 수정 오류", e);
			throw e;
		}
		
		return new ResponseEntity<>(map, HttpStatus.OK);			
	}
	
	@DeleteMapping("/menu/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable int id){
		Map<String, String> map = new HashMap<>();
		log.debug("DELETE /menu/{}", id);		
		try {
			//업무로직
			int result = menuService.deleteMenu(id);			
			if(result == 0)
				//throw new MenuNotFoundException("id - " + id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
			map.put("msg", "메뉴가 정상적으로 삭제되었습니다.");			
		} catch (Exception e) {
			log.error("메뉴 삭제 오류", e);
			throw e;
		}
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

}
