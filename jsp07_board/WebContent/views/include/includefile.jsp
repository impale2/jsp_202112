<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var ="path" value="${pageContext.request.contextPath }" />
<script type="text/javascript">
/* 파라메터의 메시지 */
if ('${param.msg}' !='')
	alert('${param.msg}');


</script>