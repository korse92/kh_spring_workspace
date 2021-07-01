<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.spring.demo.model.vo.Dev"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String[] langArr = ((Dev)request.getAttribute("dev")).getLang();
	List<String> langList = langArr != null ? Arrays.asList(langArr) : null;
	//System.out.println(langList);
	
	pageContext.setAttribute("langList", langList);
%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Demo" name="title"/>
</jsp:include>
<style>
div#demo-container{
	width:550px;
	padding:15px;
	border:1px solid lightgray;
	border-radius: 10px;
}
</style>
<div id="demo-container" class="mx-auto">
	<!-- https://getbootstrap.com/docs/4.1/components/forms/#readonly-plain-text -->
	<form id="devFrm"
		  name ="devFrm"
		  method="post" 
		  action="${pageContext.request.contextPath}/demo/updateDev.do">
		<div class="form-group row">
		  <label for="name" class="col-sm-2 col-form-label">이름</label>
		  <div class="col-sm-10">
		    <input type="text" class="form-control" id="name" name="name" value="${dev.name}" required>
		  </div>
		</div>
		<div class="form-group row">
		  <label for="career" class="col-sm-2 col-form-label">개발경력</label>
		  <div class="col-sm-10">
		    <input type="number" class="form-control" id="career" name="career" value="${dev.career}" required>
		  </div>
		</div>
		<div class="form-group row">
		  <label for="email" class="col-sm-2 col-form-label">이메일</label>
		  <div class="col-sm-10">
		    <input type="email" class="form-control" id="email" name="email" value="${dev.email}" required>
		  </div>
		</div>
	  	<!-- https://getbootstrap.com/docs/4.1/components/forms/#inline -->
	    <div class="form-group row">
	    	<label class="col-sm-2 col-form-label">성별</label>
	    	<div class="col-sm-10">
			    <div class="form-check form-check-inline">
				  <input class="form-check-input" type="radio" name="gender"
				  		 id="gender0" value="M" ${dev.gender == "M" ? "checked" : ""}>
				  <label class="form-check-label" for="gender0">남</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="radio" name="gender"
				  		 id="gender1" value="F" ${dev.gender eq 'F' ? 'checked' : ''}>
				  <label class="form-check-label" for="gender1">여</label>
				</div>
			</div>
		</div>
		<div class="form-group row">
			<label class="col-sm-2 col-form-label">개발언어</label>
			<div class="col-sm-10">
				<div class="form-check form-check-inline">
				  <br>
				  <input class="form-check-input" type="checkbox" name="lang" id="Java" value="Java" ${langList.contains('Java') ? 'checked' : ''}>
				  <label class="form-check-label" for="Java">Java</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="C" value="C" ${langList.contains('C') ? 'checked' : ''}>
				  <label class="form-check-label" for="C">C</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Javascript" value="Javascript" ${langList.contains('Javascript') ? 'checked' : ''}>
				  <label class="form-check-label" for="Javascript">Javascript</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Python" value="Python" ${langList.contains('Python') ? 'checked' : ''}>
				  <label class="form-check-label" for="Python">Python</label>
				</div>
			</div>
		</div>
		<!-- 중요 - 수정시 반드시 고유번호도 함께 넘겨주어야 함 -->
	  	<input type="hidden" name="no" value="${dev.no}" />
	  	<button type="submit" class="list-group-item list-group-item-action">dev 수정</button>
	</form>
</div>

<script>

/*
	화살표(=>) 함수 : 익명함수를 대체하기 위해 사용
	익명함수와 화살표함수 비교 예) function(){} 을 () => {} 으로 선언
	
	작성법
	1. function키워드 생략, 매개변수부 => 몸통부
		var foo = (a, b) => {
			console.log(a, b);
		}
		
	2. 매개변수가 한개인 경우, 괄호생략 가능
		var bar = x => {
			return x * x;
		}
		
	3. 몸통부 코드가 한줄(리턴포함)인 경우, {}(함수몸통)와 return 키워드 생략 가능
		-> return 키워드 제외한 코드만 작성하면된다.
		bar = x => x * x;
		var coo = () => console.log("hello"); //리턴절을 작성한 것이다.
		
	4. 화살표함수 안의 this는 보통 window이다.
	   this가 없고, parent scope this를 가져다 쓴다.
	   event handler로 사용시에도 this는 event. target이 아니다.
		var too = () => console.log(this); //window객체가 출력된다.
	
	* 화살표 함수는 메소드로써 사용하면 안된다.
	예)객체선언1
	var user = {
		name : "홍길동",
		say : function() {
			console.log(this.name + "이 블라블라~");
		}
	}; //this.name은 객체의 name을 가르킨다.
	
	예)객체선언2
	var user = {
		name : "홍길동",
		say() { //메소드 축약형(객체 안에서만 사용가능하다)
			console.log(this.name + "이 블라블라~");
		}
	};
	
	예)객체선언3 : 화살표함수로 this 사용
	var user = {
		name : "홍길동",
		say : () => {
			console.log(this.name + "이 블라블라~");
		}
	}; //this는 window객체를 가르켜 name이라는 속성은 존재하지 않는다.			
	   
 */

/**
 * 수정폼 유효성검사
 * 값이 유요하지 않을 경우
 * 1. return false
 * 2. event.preventDefault()
 */
//document.devFrm <- name값일때 이와 같이 객체를 가리킬 수 있다.
$(devFrm).submit((e) => {
	//1. 이름은 한글 2글자 이상이어야 한다.
	var $devName = $("#name");
    if(/^[가-힣]{2,}$/.test($devName.val()) == false) {
    	alert('이름은 한글 2글자 이상 작성해주세요.');		
		e.preventDefault();
    }
	
	//2. 개발언어는 하나이상 선택해야 한다.
	if(!($("[name=lang]:checked").length)) {
		alert('개발언어는 하나이상 선택해주세요');		
		e.preventDefault();
	}
	
});
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
