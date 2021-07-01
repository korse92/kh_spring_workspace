package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.HelloSpringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ServletContext servletContext; //application 객체

	@GetMapping("/boardList.do")
	public void boardList(
			@RequestParam(defaultValue = "1") int cPage,
			Model model,
			HttpServletRequest request) {
		//1. 사용자 입력값
		int numPerPage = 5;
		log.debug("cPage = {}", cPage);
		Map<String, Object> param = new HashMap<>();
		param.put("numPerPage", numPerPage);
		param.put("cPage", cPage);

		//2. 업무로직
		//a. contents영역 : mybatis의 RowBounds
		List<Board> list = boardService.selectBoardList(param);
		log.debug("list = {}", list);

		//b. pagebar영역
		int totalContents = boardService.getTotalContents();
		String url = request.getRequestURI();
		log.debug("totalContents = {}", totalContents);
		log.debug("url = {}", url);
		String pageBar = HelloSpringUtils.getPageBar(totalContents, cPage, numPerPage, url);

		//3. jsp위임 처리
		model.addAttribute("list", list);
		model.addAttribute("pageBar", pageBar);
	}

	@GetMapping("/boardForm.do")
	public void boardForm() {

	}

	/**
	 * 1. form[enctype=multipart/form-data]
	 * 2. handler - @RequestParam MultipartFile upFile
	 * 3. 서버 컴퓨터에 파일 저장
	 * 		- saveDirectory (/resources/upload/board/20210427_12345_234.jpg
	 * 		- 파일명 재지정
	 * 4. DB attachment에 저장된 파일정보 등록
	 * 		- Attachment객체 생성 --> List<Attachment> --> board.setAttachList
	 *
	 * @param board
	 * @param redirectAttr
	 * @return
	 */
	@PostMapping("/boardEnroll.do")
	public String boardEnroll(
			@ModelAttribute Board board,
			@RequestParam(value="upFile", required = false) MultipartFile[] upFiles,
			HttpServletRequest request,
			RedirectAttributes redirectAttr) {

		try {
			log.debug("board = {}", board);
			//0.파일 저장 및 Attachment객체 생성
			//저장경로 (웹루트에서 찾는 것)
			String saveDirectory =
					request.getServletContext().getRealPath("/resources/upload/board");

			//File객체를 통해서 directory 생성 가능
			File dir = new File(saveDirectory);
			if(!dir.exists())
				dir.mkdirs(); //복수개 폴더 생성 가능

			//복수개의 Attachment객체를 담을 list생성
			List<Attachment> attachList = new ArrayList<>();

			for(MultipartFile upFile : upFiles) {
				if(upFile.isEmpty() || upFile.getSize() == 0)
					continue;
				log.debug("upFile = {}", upFile);
				log.debug("upFile.name = {}", upFile.getOriginalFilename());
				log.debug("upFile.size = {}", upFile.getSize());
				//저장할 파일명 생성
				File renamedFile = HelloSpringUtils.getRenamedFile(saveDirectory, upFile.getOriginalFilename());
				//파일 저장
				upFile.transferTo(renamedFile);
				//Attachment객체 생성
				Attachment attach = new Attachment();
				attach.setOriginalFileName(upFile.getOriginalFilename());
				attach.setRenamedFileName(renamedFile.getName());
				attachList.add(attach);
			}

			//1. 업무로직
			board.setAttachList(attachList);
			int result = boardService.insertBoard(board);

			//2. 사용자피드백
			String msg = result > 0 ? "게시글 등록 성공!" : "게시글 등록 실패!";
			redirectAttr.addFlashAttribute("msg", msg);

		} catch (IOException | IllegalStateException e) {
			log.error("첨부파일 등록 오류!", e);
			throw new BoardException("첨부파일 등록 오류!"); //Checked Exception은 throw로 바로 던질수 없으니, 커스팀 예외 객체를 만들어 던져준다.
		} catch (Exception e) {
			log.error("게시물 등록 오류!", e);
			throw e;
		}

		//리다이렉트
		return "redirect:/board/boardList.do";
	}

	@GetMapping("/boardDetail.do")
	public void boardDetail(@RequestParam int no, Model model) {
		//1. 업무로직
//		Board board = boardService.selectOneBoard(no);
		Board board = boardService.selectOneBoardCollection(no);
		log.debug("board = {}", board);

		//2. jsp 위임
		model.addAttribute("board", board);
	}

	/**
	 * Resource 추상체
	 * - Web상의 자원, classpath, filesystem자원을 모두 처리할수 있는 추상레이어(PSA)
	 *
	 * @ResponseBody 현재메소드의 리턴객체를 http응답메시지 body에 직접 출력.
	 *
	 * @param no
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(
			value =	"/fileDownload.do",
			produces = "application/octet-stream; charset=utf-8"
		)
	@ResponseBody
	public Resource fileDownload(@RequestParam int no, HttpServletResponse response)
			throws UnsupportedEncodingException {

		//1. 업무로직
		Attachment attach = boardService.selectOneAttachment(no);
		log.debug("attach = {}", attach);

		String originalFileName = attach.getOriginalFileName();
		String renamedFileName = attach.getRenamedFileName();

		//2. Resource준비
		String saveDirectory = servletContext.getRealPath("/resources/upload/board");
		File downFile = new File(saveDirectory, renamedFileName); //실제 파일을 가리키는 File 객체 생성
		String location = "file:" + downFile; //File의 toString()은 실제파일의 전체경로 주소를 반환함.
		log.debug("location = {}", location);
		//Resource resource = resourceLoader.getResource(location);
		Resource resource = new FileSystemResource(downFile);

		//3. 응답헤더
		//한글파일 깨짐 방지
		originalFileName = new String(originalFileName.getBytes("utf-8"), "iso-8859-1");
		response.setContentType("application/octet-stream; charset=utf-8");
		response.addHeader("Content-Disposition", "attachment;filename=\"" + originalFileName +"\"");

		return resource;
	}

	/**
	 * ResponseEntity
	 * (@ResponseBody 기능 포함 - 응답http메세지 body에 리턴된 객체를 직접 작성)
	 * 1. html이 아닌 data를 통신 - 리턴객체를 응답메세지 body에 직접 작성
	 * 		- generic type객체가 출력됨.
	 * 2. 응답헤더관련 내용을 직접 제어
	 *
	 * 객체생성 방법
	 * 1. build방식
	 * 2. new 연산자를 통한 생성자 호출방식
	 *
	 * @param no
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/responseEntity/fileDownload.do")
	public ResponseEntity<Resource> fileDownload(@RequestParam int no) throws UnsupportedEncodingException {
		//1. 업무로직
		Attachment attach = boardService.selectOneAttachment(no);

		if(attach == null)
			return ResponseEntity.notFound().build(); //status:404를 보냄

		//2. Resource객체
		String saveDirectory = servletContext.getRealPath("/resources/upload/board");
		File downFile = new File(saveDirectory, attach.getRenamedFileName());
		Resource resource = resourceLoader.getResource("file:" + downFile);

		//3. ResponseEntity객체
		//한글파일 깨짐 방지
		String originalFileName = attach.getOriginalFileName();
		originalFileName = new String(originalFileName.getBytes("utf-8"), "iso-8859-1");

		return ResponseEntity
						.ok() //statusCode:200
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"" + originalFileName + "\"")
						.body(resource);
	}

	@GetMapping("/searchTitle.do")
	public ResponseEntity<?> autoComplete(@RequestParam String searchTitle) {
		//1. 업무로직
		List<Map<String, Object>> list = boardService.selectBoardList(searchTitle);

		log.debug("map = {}", list);

		//2. json변환 객체
		return ResponseEntity.ok()
							 .body(list);
	}

}
