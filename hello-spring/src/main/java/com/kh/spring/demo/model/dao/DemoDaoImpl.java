package com.kh.spring.demo.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.demo.model.vo.Dev;


//DAO는 @Repository 어노테이션 사용
@Repository
public class DemoDaoImpl implements DemoDao {
	
	//Autowired 어노테이션을 사용하여 SqlSessionTemplate타입으로 SqlSession 객체 사용
	@Autowired
	private SqlSessionTemplate session;

	@Override
	public int insertDev(Dev dev) {
		return session.insert("demo.insertDev", dev);
	}

	@Override
	public List<Dev> selectDevList() {
		return session.selectList("demo.selectDevList");
	}

	@Override
	public Dev selectDevOne(int no) {
		return session.selectOne("demo.selectDevOne", no);
	}

	@Override
	public int updateDev(Dev dev) {
		return session.update("demo.updateDev", dev);
	}

	@Override
	public int deleteDev(int no) {
		return session.delete("demo.deleteDev", no);
	}

}
