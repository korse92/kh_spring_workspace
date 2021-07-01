package com.kh.spring.menu.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.menu.model.vo.Menu;

@Repository
public class MenuDaoImpl implements MenuDao {
	
	@Autowired
	SqlSessionTemplate session;

	@Override
	public List<Menu> selectMenuList() {
		return session.selectList("menu.selectMenuList");
	}

	@Override
	public List<Menu> selectMenuListByType(String type) {
		return session.selectList("menu.selectMenuListByType", type);
	}

	@Override
	public int insertMenu(Menu menu) {
		return session.insert("menu.insertMenu", menu);
	}

	@Override
	public Menu selectOneMenu(int id) {
		return session.selectOne("menu.selectOneMenu", id);
	}

	@Override
	public int updateMenu(Menu menu) {
		return session.update("menu.updateMenu", menu);
	}

	@Override
	public int deleteMenu(int id) {
		return session.delete("menu.deleteMenu", id);
	}
	
}
