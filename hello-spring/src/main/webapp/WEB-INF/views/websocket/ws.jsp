<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Hello Websocket" name="title"/>
</jsp:include>
<div class="input-group mb-3">
  <input type="text" id="message" class="form-control" placeholder="Message...">
  <div class="input-group-append" style="padding: 0px;">
    <button id="sendBtn" class="btn btn-outline-secondary" type="button">Send</button>
  </div>
</div>
<div>
	<ul class="list-group list-group-flush" id="data"></ul>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js" integrity="sha512-hsqWiVBsPC5Hz9/hy5uQX6W6rEtBjtI8vyOAR4LAFpZAbQjJ43uIdmKsA7baQjM318sf8BBqrMkcWsfSsaWCNg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
//websocket객체 생성
//const ws = new WebSocket(`ws://\${location.host}${pageContext.request.contextPath}/mmm`);

//http프로토콜로 통신시작,
//1. websocket사용가능한 경우는 ws프로토콜 업그레이드
//2. (fallback) websocket 지원하지 않을 경우 xhr-stream, xhr-polling중에 적합한 방식으로 통신
//const ws = new SockJS(`http://\${location.host}${pageContext.request.contextPath}/mmm`); //sockjs로 웹소켓 객체 생성
const ws = new SockJS("http://" + location.host + "${pageContext.request.contextPath}/mmm"); //sockjs로 웹소켓 객체 생성

ws.onopen = e => {
	console.log("onopen : ", e);
};
ws.onmessage = e => {
	console.log("onmessage : ", e);
	const obj = JSON.parse(e.data);
	console.log(obj);
	const {id, message, type, time} = obj; //구조분해 할당

	const $data = $("#data"); //ul태그
	$data.append(`<li class="list-group-item"> \${id} : \${message} </li>`);
};
ws.onerror = e => {
	console.log("onerror : ", e);
};
ws.onclose = e => {
	console.log("onclose : ", e);
};

$("#sendBtn").click(() => {
	const $message = $("#message");
	$message.val() != '' && sendMessage();
});

function sendMessage(){
	const $message = $("#message");
	ws.send($message.val());
	$message.val('');
}

</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>