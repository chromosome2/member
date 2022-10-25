<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�� �� ����</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function readImage(input){
		if(input.files && input.files[0]){
			let reader=new FileReader();
			reader.onload=function(event){
				$('#preview').attr('src',event.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	function toList(obj){
		obj.action="${contextPath}/board/listArticles.do";
		obj.submit();
	}
</script>
</head>
<body>
	<form name="frmArticle" action="${contextPath }" method="post" enctype="multipart/form-data"><%--�ϳ��� �����ȿ� ���� �������� name�� �� �༭ �����ؾ���. --%>
		<table align="center">
			<tr>
				<td width="150" align="center" bgcolor="#ff9933">�۹�ȣ</td>
				<td><input type="text" value="${article.articleNo }" name="articleNo" disabled></td><%--�������Ѱ� �Ѿ���°���. name�� �־�� �����Ҷ� ������������.--%>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#ff9933">�ۼ��ھ��̵�</td>
				<td><input type="text" value="${article.id}" disabled name="writer"></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#ff9933">����</td>
				<td><input type="text" value="${article.title}" disabled name="title"></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#ff9933">����</td>
				<td>
					<textarea rows="20" cols="60" name="content" disabled>${article.content }</textarea>
				</td>
			</tr>
			<c:if test="${not empty article.imageFileName && article.imageFileName != 'null'}"><%--imageFileName�� ������� �ʴٸ� --%>
				<tr>
					<td width="150" rowspan="2" align="center" bgcolor="#ff9933">�̹���</td>
					<td>
						<img src="${contextPath }/download.do?articleNo=${article.articleNo}&imageFileName=${article.imageFileName}" id="preview" width="300"><br> 
					</td>
				</tr>
				<tr>
					<td><input type="file" name="imageFileName" onchange="readImage(this)" disabled></td>
				</tr>
			</c:if>
			<tr>
				<td width="150" align="center" bgcolor="#ff9933">�������</td>
				<td><input type="text" value="<fmt:formatDate value="${article.writeDate}"/>" disabled></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="�����ϱ�" onclick="">
					<input type="button" value="�����ϱ�" onclick="">
					<input type="button" value="����Ʈ�� ���ư���" onclick="toList(this.form)">
					<input type="button" value="��۾���" onclick="">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>