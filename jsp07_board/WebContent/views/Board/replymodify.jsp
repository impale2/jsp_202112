<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../include/includefile.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 수정</title>
<script type="text/javascript">
function modifyCheck(){
	var email = frmModify.email.value;
	var subject = frmModify.subject.value;
	var content = frmModify.content.value;
	console.log(email);
	console.log(subject);
	console.log(content);
	if (email==''){
		alert('이메일을 입력해주세요!');
		frmModify.email.focus();
	}else if (subject == ''){
		alert('제목을 입력해주세요!');
		frmModify.subject.focus();
	}else if (content == ''){
		alert('내용을 입력해주세요!');
		frmModify.content.focus();
	}else{
		frmModify.submit();
	}
	
}
</script>
</head>
<body>
	<h2>댓글 수정</h2>
	<form name = "frmModify" action="${path}/reply/modify" enctype="multipart/form-data" method="post">
	<table border="1">
			<tr>
				<th>번호</th>
				<td> <input type="text" name="bnum" value="${board.bnum}" readonly ></td>
			</tr>
			<tr>
				<th>이메일</th>
				<td> <input type="text" name="email" value="${board.email}" readonly></td>
			</tr>
			<tr>
				<th>제목</th>
				<td> <input type="text" name="subject" value="${board.subject}"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td> <textarea rows="5" cols="25" name="content">${board.content}</textarea> </td>
			</tr>
			<tr>
			<td colspan="2" align="center">	
				<button type="button" onclick="modifyCheck()">수정</button>
				<button type="reset">취소</button>
			</td>	
			</tr>
	</table>
	</form>
</body>
</html>