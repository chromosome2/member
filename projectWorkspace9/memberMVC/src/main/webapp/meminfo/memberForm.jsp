<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/> <!-- value를 변수(var)에 세팅. 다른 페이지에서 쓸수 있게. 127.0.0.1:8090/프로젝트이름까지,,, 들어있는거임. ex)127.0.0.1:8090/memberMVC-->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입창</title>
</head>
<body>
	<form action="${contextPath }/member/addMember.do" method="post"> <!-- method안쓰면 get. get은 주소창에 같이 날아감. 개인정보가 주소창에 뜨면 안되니까 post.-->
		<h2 align="center">회원 가입창</h2>
		<table align="center">
			<tr>
				<td width="200"><p align="right">아이디</p></td>
				<td width="400"><input type="text" name="id"/></td>
			</tr>
			<tr>
				<td width="200"><p align="right">비밀번호</p></td>
				<td width="400"><input type="password" name="pwd"/></td>
			</tr>
			<tr>
				<td width="200"><p align="right">이름</p></td>
				<td width="400"><input type="text" name="name"/></td>
			</tr>
			<tr>
				<td width="200"><p align="right">이메일</p></td>
				<td width="400"><input type="text" name="email"/></td>
			</tr>
			<tr>
				<td width="200">&nbsp;</td>
				<td width="400">
					<input type="submit" value="가입하기">
					<input type="reset" value="다시입력"> 
				</td>
			</tr>
		</table>
	</form>
</body>
</html>