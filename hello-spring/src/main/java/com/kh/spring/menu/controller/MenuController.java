package com.kh.spring.menu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
public class MenuController {

	@GetMapping("/menu/menu.do")
	public void menu() {}
	
}
