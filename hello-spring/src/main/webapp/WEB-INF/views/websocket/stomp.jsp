<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Hello Stomp" name="title"/>
</jsp:include>
<div class="input-group mb-3">
  <select id="stomp-url" class="form-select mr-1">
    <option value="">전송url</option>
    <option value="/app/a">/app/a</option>
    <option value="/topic">/topic</option>
  </select>
  <input type="text" id="message" class="form-control" placeholder="Message...">
  <div class="input-group-append" style="padding: 0px;">
    <button id="sendBtn" class="btn btn-outline-secondary" type="button">Send</button>
  </div>
</div>
<div>
	<ul class="list-group list-group-flush" id="data"></ul>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js" integrity="sha512-hsqWiVBsPC5Hz9/hy5uQX6W6rEtBjtI8vyOAR4LAFpZAbQjJ43uIdmKsA7baQjM318sf8BBqrMkcWsfSsaWCNg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
//1.웹소켓객체 -> stomp객체에 전달
const ws = new SockJS("${pageContext.request.contextPath}/stomp");
const stompClient = Stomp.over(ws);

//2.connect핸들러 작성. 구독(등록된 SimpleBroker url로 작성)
stompClient.connect({}, (frame) => {
	console.log("stomp connected : ", frame);
	
	stompClient.subscribe("/topic", (message) => {
		console.log("message from /topic : ", message);
	});
	
	stompClient.subscribe("/appppp", (message) => {
		console.log("message from /app : ", message);
	});
});

//3.메세지 발행
$("#sendBtn").click(() => {
	const $message = $("#message");
	const $url = $("#stomp-url"); //$("#stomp-url option:selected")
	
	if($message.val() == '') return;
	if($url.val() == ''){
		alert("전송 url을 선택하세요.");
		return;
	}
	
	sendMessage($url.val());
});

function sendMessage(url) {
	//url = url || "/app"; //url이 undefined이면, /app
	stompClient.send(url, {}, $("#message").val());
	
	$("#message").val(''); //초기화
}

</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
