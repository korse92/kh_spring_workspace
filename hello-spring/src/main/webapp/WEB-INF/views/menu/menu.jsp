<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:requestEncoding value="utf-8" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="메뉴REST" name="title" />
</jsp:include>

<style>
div#menu-container {
	text-align: center;
}

div.menu-test {
	width: 50%;
	margin: 0 auto;
	text-align: center;
}

div.result {
	width: 70%;
	margin: 0 auto;
}
</style>
<script>
const menuRestApiHost = "http://localhost:10000/springboot/";

function displayResultTable(selector, data) {
	const $container = $(selector);
	const $table = $("<table></table>").addClass("table");
	const $header = $("<tr><th>번호</th><th>음식점</th><th>메뉴명</th><th>가격</th><th>타임</th><th>맛</th></tr>");
	$table.append($header);
	
	
	if(data.length > 0) {
		$(data).each((i, {id, restaurant, name, price, type, taste}) => {
			const tr = `<tr>
				<td>\${id}</td>
				<td>\${restaurant}</td>
				<td>\${name}</td>
				<td>\${price}</td>
				<td>\${type}</td>
				<td>\${taste}</td>
			</tr>`;
			$table.append(tr);
		});		
	} else {
		$table.append("<tr><td colspan='6'>조회된 결과가 없습니다.</td></tr>");
	}
	
	$container.html($table);
}
</script>

<div id="menu-container">
	<!-- 1.GET menus-->
	<div class="menu-test">
		<h4>전체메뉴조회(GET)</h4>
		<input type="button"
			class="btn btn-block btn-outline-success btn-send" id="btn-menus"
			value="전송" />
	</div>
	<div class="result" id="menus-result"></div>
	<script>
	/**
	* Access to XMLHttpRequest at 'http://localhost:10000/springboot/menus'
	* from origin 'http://localhost:9090'
	* has been blocked
	* by CORS policy:
	* No 'Access-Control-Allow-Origin' header is present on the requested resource.
	* 
	* SOP Same Origin Policy 동일근원정책
	* 비동기 요청시 같은 Origin으로만 요청을 보낼 수 있다.
	* origin : protocol +	host	+ post
	*		   http:// 	  localhost   :9090
	*
	* CORS Cross Origin Resource Sharing
	* 응답헤더에 Access-Control-Allow-Origin에 현재 Origin을 허용할때는 CORS가 가능하다.
	* Access-Control-Allow-Origin : http://localhost:9090
	* Access-Control-Allow-Origin : * (모든 Origin)
	*/
	$("#btn-menus").click(e => {
		$.ajax({
	 	url: `\${menuRestApiHost}/menus`,
	 	method: "GET",
	 	dataType: "json",
	 	success: data => {
	 		console.log(data);
	 		displayResultTable("#menus-result", data);
	 	},
	 	/* error: (xhr, status, err) => console.log(xhr, status, err) */
	 	error: console.log//error함수의 모든 인자를 console.log에 전달
		});	        	
	});
	</script>

	<!-- 
		2.GET /menus/kr /menus/ch /menus/jp 타입별 조회
		@실습문제 : 음식 타입 조회
		@PathVariable을 사용할 것. 
	-->
	<div class="menu-test">
		<h4>메뉴 타입별 조회(GET)</h4>
		<select class="form-control" id="typeSelector">
			<option value="" disabled selected>음식타입선택</option>
			<option value="kr">한식</option>
			<option value="ch">중식</option>
			<option value="jp">일식</option>
		</select>
	</div>
	<div class="result" id="menuType-result"></div>
	<script>
	$("#typeSelector").change(e => {
		const type = $(e.target).val();
		//console.log(type);
		
		$.ajax({
			url: `\${menuRestApiHost}/menus/\${type}`,
			dataType: "json",
			success(data){
				console.log(data);
				displayResultTable("#menuType-result", data);
			},
			error(xhr, status, err) {
				console.log(xhr, status, err);
			}
		});
	});
	</script>
	<!-- 2.POST -->
	<div class="menu-test">
		<h4>메뉴 등록하기(POST)</h4>
		<form id="menuEnrollFrm">
			<input type="text" name="restaurant" placeholder="음식점"
				class="form-control" /> <br /> <input type="text" name="name"
				placeholder="메뉴" class="form-control" /> <br /> <input
				type="number" name="price" placeholder="가격" class="form-control" />
			<br />
			<div class="form-check form-check-inline">
				<input type="radio" class="form-check-input" name="type"
					id="post-kr" value="kr" checked> <label for="post-kr"
					class="form-check-label">한식</label>&nbsp; <input type="radio"
					class="form-check-input" name="type" id="post-ch" value="ch">
				<label for="post-ch" class="form-check-label">중식</label>&nbsp; <input
					type="radio" class="form-check-input" name="type" id="post-jp"
					value="jp"> <label for="post-jp" class="form-check-label">일식</label>&nbsp;
			</div>
			<br />
			<div class="form-check form-check-inline">
				<input type="radio" class="form-check-input" name="taste"
					id="post-hot" value="hot" checked> <label for="post-hot"
					class="form-check-label">매운맛</label>&nbsp; <input type="radio"
					class="form-check-input" name="taste" id="post-mild" value="mild">
				<label for="post-mild" class="form-check-label">순한맛</label>
			</div>
			<br /> <input type="submit"
				class="btn btn-block btn-outline-success btn-send" value="등록">
		</form>
	</div>
	<script>
	$("#menuEnrollFrm").submit(e => {
		//폼제출 방지
		e.preventDefault();
		//$("선택자", elem) : elem 하위의 요소를 찾게 함
		const restaurant = $("[name=restaurant]", e.target).val();
		const name = $("[name=name]", e.target).val();
		const price = $("[name=price]", e.target).val();
		const type = $("[name=type]:checked", e.target).val();
		const taste = $("[name=taste]:checked", e.target).val();
		
		//유효성검사
		if(!restaurant || !name || !price){
			alert("입력폼을 모두 작성하세요.");
			return;
		}
		
		const menu = {restaurant, name, price, type, taste};
		console.log(menu);
		
		//서버에 json형식으로 데이터 전달(요청메세지 body)
		$.ajax({
			url: `\${menuRestApiHost}/menu`,
			method: "POST",
			data: JSON.stringify(menu),
			contentType: "application/json; charset=utf-8",
			success({msg}){
				//console.log(data);
				alert(msg);
			},
			error(xhr, status, err) {
				alert("메뉴 등록 실패 : " + status);
				console.log(xhr, status, err);
			},
			complete(){
				//폼 초기화
				$(e.target)[0].reset();//자바스크립트 함수
			}
		});
		
	});
	</script>

	<!-- #3.PUT -->
	<!-- 
		@실습문제: GET /menu/3 조회후, #menuUpdateFrm에 데이터 시각화할 것
		 존재하지 않는 메뉴인 경우, 404을 응답하고, 사용자 피드백 줄 것.
	-->
	<div class="menu-test">
		<h4>메뉴 수정하기(PUT)</h4>
		<p>메뉴번호를 사용해 해당메뉴정보를 수정함.</p>
		<form id="menuSearchFrm">
			<input type="text" name="id" placeholder="메뉴번호" class="form-control" /><br />
			<input type="submit"
				class="btn btn-block btn-outline-primary btn-send" value="검색">
		</form>
		<hr />
		<form id="menuUpdateFrm">
			<!-- where조건절에 사용할 id를 담아둠 -->
			<input type="hidden" name="id" /> <input type="text"
				name="restaurant" placeholder="음식점" class="form-control" /> <br />
			<input type="text" name="name" placeholder="메뉴" class="form-control" />
			<br /> <input type="number" name="price" placeholder="가격"
				step="1000" class="form-control" /> <br />
			<div class="form-check form-check-inline">
				<input type="radio" class="form-check-input" name="type" id="put-kr"
					value="kr" checked> <label for="put-kr"
					class="form-check-label">한식</label>&nbsp; <input type="radio"
					class="form-check-input" name="type" id="put-ch" value="ch">
				<label for="put-ch" class="form-check-label">중식</label>&nbsp; <input
					type="radio" class="form-check-input" name="type" id="put-jp"
					value="jp"> <label for="put-jp" class="form-check-label">일식</label>&nbsp;
			</div>
			<br />
			<div class="form-check form-check-inline">
				<input type="radio" class="form-check-input" name="taste"
					id="put-hot" value="hot" checked> <label for="put-hot"
					class="form-check-label">매운맛</label>&nbsp; <input type="radio"
					class="form-check-input" name="taste" id="put-mild" value="mild">
				<label for="put-mild" class="form-check-label">순한맛</label>
			</div>
			<br /> <input type="submit"
				class="btn btn-block btn-outline-success btn-send" value="수정">
		</form>
	</div>
	<script>
	$(menuUpdateFrm).submit(e => {
		e.preventDefault();
		const $frm = $(e.target);
		const id = Number($frm.find("[name=id]").val());
		const restaurant = $frm.find("[name=restaurant]").val();
		const name = $frm.find("[name=name]").val();
		const price = Number($frm.find("[name=price]").val());
		const type = $frm.find("[name=type]:checked").val();
		const taste = $frm.find("[name=taste]:checked").val();
		
		const menu = {id, restaurant, name, price, type, taste};
		console.log("menu = ", menu);
		
		//415 Unsupported Media Type - https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/415
		//contentType에 맞지않는 데이터를 보내면 오는 에러코드
		$.ajax({
			url: `\${menuRestApiHost}/menu`,
			data: JSON.stringify(menu),
			contentType: "application/json; charset=utf-8",
			method: "PUT",
			success(data){
				console.log(data);
				const {msg} = data;
				alert(msg);
			},
			error(xhr, status, err){
				alert("메뉴 수정 오류 : " + status);
				console.log(xhr, status, err);
			},
			complete(){
				$frm[0].reset();//폼 초기화
				$(menuSearchFrm)[0].reset();
			}
		});
		
	});
	
	$("#menuSearchFrm").submit(e => {
		e.preventDefault();
		const id = $("[name=id]", e.target).val();
		
		if(!id){
			alert("메뉴번호를 입력해주세요.");
			return
		}
		
		$.ajax({
			url: `\${menuRestApiHost}/menu/\${id}`,
			success(data){
				console.log(data);
				
				const $frm = $(menuUpdateFrm);
				
				const {id, restaurant, name, price, type, taste} = data;
				$("[name=id]", $frm).val(id);
				$("[name=restaurant]", $frm).val(restaurant);
				$frm.find("[name=name]").val(name);
				$frm.find("[name=price]").val(price);
				$(`[name=type][value=\${type}]`, $frm).prop("checked", true);
				$(`[name=taste][value=\${taste}]`, $frm).prop("checked", true);							
			},
			error(xhr, statusText, err){
				console.log(xhr, statusText, err);
				const {status} = xhr;
				//좌항이 true일때 실행 (<-> || : 우항이 false일때 실행)
				status === 404 && alert("조회한 메뉴는 존재하지 않습니다.");
				$("[name=id]", e.target).select();
			}
		});
	});
	</script>

	<!-- @실습문제 : DELETE /menu/3 -->
	<div class="menu-test">
		<h4>메뉴 삭제하기(DELETE)</h4>
		<p>메뉴번호를 사용해 해당메뉴정보를 삭제함.</p>
		<form id="menuDeleteFrm">
			<input type="text" name="id" placeholder="메뉴번호" class="form-control" /><br />
			<input type="submit"
				class="btn btn-block btn-outline-danger btn-send" value="삭제">
		</form>
	</div>
	<script>
    $(menuDeleteFrm).submit(e => {
    	e.preventDefault();
    	const id = $("[name=id]", e.target).val();
    	
    	$.ajax({
    		url: `\${menuRestApiHost}/menu/\${id}`,
    		method: "DELETE",
    		success(data){
    			console.log(data);
    			const {msg} = data;
    			alert(msg);
    		},
    		error(xhr, statusText, err){
    			const {status} = xhr;
    			switch(status){
    			case 404: alert("해당 메뉴는 존재하지 않습니다."); break;
    			default: alert(`메뉴 삭제 실패 코드 \${status} : \${statusText}`);
    			}
    			console.log(xhr, statusText, err);
    		},
    		complete(){
				e.target.reset();//폼 초기화
			}
    	})
    });
    </script>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>