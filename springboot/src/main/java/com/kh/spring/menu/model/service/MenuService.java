package com.kh.spring.menu.model.service;

import java.util.List;

import com.kh.spring.menu.model.vo.Menu;

public interface MenuService {

	List<Menu> selectMenuList();

	List<Menu> selectMenuListByType(String type);

	int insertMenu(Menu menu);

	Menu selectOneMenu(int id);

	int updateMenu(Menu menu);

	int deleteMenu(int id);

}
