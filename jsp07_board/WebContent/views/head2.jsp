<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="../views/include/includefile.jsp" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	console.log('${sessionScope.email}');
	console.log('${sessionScope}');
</script>
<style>
	header{
		display: flex;
		
		
	}

</style>
</head>
<body>
	<header>
		<div> 
			<span><a href="${path}/member/myinfo">${sessionScope.email}</a> </span>
			<!-- empty sessionScope.email : null체크 -->
			<c:if test="${empty sessionScope.email}">
				<a href="${path}/views/member/login.jsp">로그인</a> 
				<a href="${path}/views/member/add.jsp">회원등록</a>  
			</c:if>
			<c:if test="${not empty sessionScope.email}">
				<a href="${path}/member/logout">로그아웃</a> 
			</c:if>

		</div>
	</header>
	<nav>
		<a href="${path}/views/home.jsp">홈</a>
	</nav>
</body>

</html>