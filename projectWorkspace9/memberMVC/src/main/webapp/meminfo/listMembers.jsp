<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 출력창</title>
<style type="text/css">
	h2{
		text-align: center;
	}
</style>
<c:choose>
	<c:when test='${msg=="addMember"}'>
		<script>
			window.onload=function () {
				alert("회원을 등록했습니다.");
			}
		</script>
	</c:when>
	<c:when test='${msg=="modified"}'>
		<script>
			window.onload=function () {
				alert("회원정보를 수정했습니다.");
			}
		</script>
	</c:when>
	<c:when test='${msg=="deleted"}'>
		<script>
			window.onload=function () {
				alert("회원정보를 삭제했습니다.");
			}
		</script>
	</c:when>
</c:choose>
</head>
<body>
	<h2>회원정보</h2>
	<table align="center" border=1>
		<tr align="center" bgcolor="lightgreen">
			<th>아이디</th><th>비밀번호</th><th>이름</th><th>이메일</th><th>가입일</th><th>수정</th><th>삭제</th>
		</tr>
		<c:choose>
			<c:when test="${empty membersList }"> <!-- 회원정보가 없을때 -->
				<tr>
					<td colspan="5" align="center">
						등록된 회원이 없습니다.
					</td>
			</c:when>
			<c:when test="${!empty membersList }">
				<c:forEach var="mem" items="${membersList }">
					<tr align="center">
						<td>${mem.id }</td>
						<td>${mem.pwd }</td>
						<td>${mem.name }</td>
						<td>${mem.email }</td>
						<td>${mem.joinDate }</td>
						<td><a href="${contextPath }/member/modMemberForm.do?id=${mem.id}">수정</a></td>
						<td><a href="${contextPath }/member/delMember.do?id=${mem.id}">삭제</a></td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<p align="center"><a href="${contextPath }/member/memberForm.do">회원가입하기</a></p>
</body>
</html>