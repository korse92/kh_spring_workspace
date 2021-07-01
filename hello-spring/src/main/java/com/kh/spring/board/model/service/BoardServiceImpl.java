package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;

	@Override
	public List<Board> selectBoardList(Map<String, Object> param) {
		return boardDao.selectBoardList(param);
	}

	@Override
	public int getTotalContents() {
		return boardDao.getTotalContents();
	}
	
	/**
	 * @Transactional
	 * - class level : 모든 메소드 실행결과 Runtime예외가 던져지면, rollback.
	 * - method level : 해당 메소드 실행결과 Runtime예외가 던져지면, rollback.
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board) {
		//모든 DML이 같은 트랜잭션에서 작업되도록 해야함
		
		int result = 0;
		//1. board객체 등록
		result = boardDao.insertBoard(board);
		//같은 트랜잭션에서 시퀀스번호를 가져온 것이라 여러 DB요청이 있을 시에도 문제가 발생하지 않는다.
		log.debug("board.no = {}", board.getNo());
		
		//2. attachment객체 등록
		//insert into attachment(no, board_no, original_filename, rename_filename)
		//values(seq_attachment_no.nextval, #{boardNo}, #{originalFileName}, #{renamedFileName}
		if(!board.getAttachList().isEmpty()) {
			for(Attachment attach : board.getAttachList()) {
				attach.setBoardNo(board.getNo());
				result = boardDao.insertAttachment(attach);
			}
		}
		return result;
	}

	@Override
	public Board selectOneBoard(int no) {
		//게시글 조회
		Board board = boardDao.selectOneBoard(no);
		//첨부파일 목록 조회
		List<Attachment> attachList = boardDao.selectAttachmentList(no);
		log.debug("attachList = {}", attachList);
		//board객체에 attachList setting
		board.setAttachList(attachList);
		
		return board;
	}

	@Override
	public Board selectOneBoardCollection(int no) {
		return boardDao.selectOneBoardCollection(no);
	}

	@Override
	public Attachment selectOneAttachment(int no) {
		return boardDao.selectOneAttachment(no);
	}

	@Override
	public List<Map<String, Object>> selectBoardList(String searchTitle) {
		return boardDao.selectBoardList(searchTitle);
	}
	
	

}
