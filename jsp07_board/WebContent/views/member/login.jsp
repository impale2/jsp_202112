<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="../include/includefile.jsp" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script type="text/javascript">

	function loginCheck() {
		var email = frmLogin.email.value;
		var passwd = frmLogin.passwd.value;
		console.log(email);
		console.log(passwd);
		if(email==''){
			alert('이메일을 입력해주세요!');
			frmLogin.email.focus();
		}else if (passwd==''){
			alert('비밀번호를 입력해주세요!');
			frmLogin.passwd.focus();
		}else{
			frmLogin.action = '${path}/member/login';
			frmLogin.submit();
		}
		
		
	}
	
</script>
</head>
<body>
<%@ include file="../header.jsp" %>
	<h2>로그인</h2>
	<%
	//쿠키를 읽기
	Cookie[] cookies = request.getCookies();
	String email=null;
	String passwd = null;
	if (cookies!=null){
		for(Cookie cookie : cookies ){
			System.out.println(cookie.getName() + ":" + cookie.getValue());
			if (cookie.getName().equals("email")){
				email = cookie.getValue();
			}else if(cookie.getName().equals("passwd")){
				passwd = cookie.getValue();
			}
			
		}
	}

%>
	<form name="frmLogin" method="post">
		<table>
			<tr>
				<td>
				이메일<input type="email" name="email" value="${cookie.email.value}">
				</td>
			</tr>
			<tr>
				<td>
				비밀번호<input type="password" name="passwd" value="${cookie.passwd.value}">
				</td>
			</tr>
			<tr>
				<td colsapn="2" align="center">
					<input type ="checkbox" name="idsave">아이디저장
					<button type="button"  onclick="loginCheck()">로그인</button>
					<a href="${path}/views/member/add.jsp"><button >회원가입</button></a>
				</td>
			</tr>
		
		</table>
	
	</form>
</body>
</html>