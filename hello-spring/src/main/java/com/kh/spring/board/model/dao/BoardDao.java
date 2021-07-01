package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;

public interface BoardDao {

	List<Board> selectBoardList(Map<String, Object> param);

	int getTotalContents();

	int insertBoard(Board board);

	int insertAttachment(Attachment attach);

	Board selectOneBoard(int no);

	List<Attachment> selectAttachmentList(int boardNo);

	Board selectOneBoardCollection(int no);

	Attachment selectOneAttachment(int no);

	List<Map<String, Object>> selectBoardList(String searchTitle);

}
