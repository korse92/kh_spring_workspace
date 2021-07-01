package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.vo.Dev;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;

	/**
	 * 1,3,4의 과정은 AOP에서 자동으로 진행해 준다.
	 */
	@Override
	public int insertDev(Dev dev) {
		//1.SqlSession객체 생성
		//2.dao업무요청
		//int result = demoDao.insertDev(dev);
		//3.트랜잭션처리(DML)
		//4.SqlSession 반납
		return demoDao.insertDev(dev);
	}

	@Override
	public List<Dev> selectDevList() {
		//1.SqlSession객체 생성
		//2.dao업무요청
		//List<Dev> list = demoDao.selectDevList();
		//3.트랜잭션처리(DML)
		//4.SqlSession 반납
		return demoDao.selectDevList();
	}

	@Override
	public Dev selectDevOne(int no) {
		return demoDao.selectDevOne(no);
	}
	
	/**
	 * transaction처리(AOP)
	 * - 예외가 발생하지 않으면 commit
	 * - 예외가 발생하면 rollback
	 */
	@Override
	public int updateDev(Dev dev) {
		return demoDao.updateDev(dev);
	}

	@Override
	public int deleteDev(int no) {
		return demoDao.deleteDev(no);
	}
}
