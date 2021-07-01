<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="게시판 상세보기" name="title"/>
</jsp:include>
<style>
div#board-container{width:400px;}
input, button, textarea {margin-bottom:15px;}
button { overflow: hidden; }
/* 부트스트랩 : 파일라벨명 정렬*/
div#board-container label.custom-file-label{text-align:left;}
</style>
<div id="board-container" class="mx-auto text-center">
	<input type="text" class="form-control" 
		   placeholder="제목" name="boardTitle" id="title" 
		   value="${board.title}" required>
	<input type="text" class="form-control" 
		   name="memberId" 
		   value="${board.memberId}" readonly required>
	
	<c:forEach items="${board.attachList}" var="attach">
	<button type="button" 
			class="btn btn-outline-success btn-block"
			onclick="fileDownload(${attach.no});">
		첨부파일 - ${attach.originalFileName}
	</button>	
	</c:forEach>
	
    <textarea class="form-control" name="content" 
    		  placeholder="내용" required>${board.content}</textarea>
    <input type="number" class="form-control" name="readCount" title="조회수"
		   value="${board.readCount}" readonly>
	<!--
		input:datetime-local은 ISO 8601형식에 맞게 dateformat을 제공해야 한다.
		https://ko.wikipedia.org/wiki/ISO_8601
		https://java119.tistory.com/m/24
	-->
	<input type="datetime-local" class="form-control" name="regDate" 
		   value='<fmt:formatDate value="${board.regDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>'>
</div>
<script>
function fileDownload(no) {
	location.href = `${pageContext.request.contextPath}/board/fileDownload.do?no=\${no}`;
	//location.href = `${pageContext.request.contextPath}/board/responseEntity/fileDownload.do?no=\${no}`;
}
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
