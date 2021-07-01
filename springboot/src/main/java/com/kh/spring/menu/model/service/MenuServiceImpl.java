package com.kh.spring.menu.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.menu.model.dao.MenuDao;
import com.kh.spring.menu.model.vo.Menu;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> selectMenuList() {
		return menuDao.selectMenuList();
	}

	@Override
	public List<Menu> selectMenuListByType(String type) {
		return menuDao.selectMenuListByType(type);
	}

	@Override
	public int insertMenu(Menu menu) {
		return menuDao.insertMenu(menu);
	}

	@Override
	public Menu selectOneMenu(int id) {
		return menuDao.selectOneMenu(id);
	}

	@Override
	public int updateMenu(Menu menu) {
		return menuDao.updateMenu(menu);
	}

	@Override
	public int deleteMenu(int id) {
		return menuDao.deleteMenu(id);
	}

}
