<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix ="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %> <%-- 날짜 사용할때 --%>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %> <%-- 변수선언 --%>
<c:set var="contextPath" value = "${pageContext.request.contextPath}" />
<%
request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기 창</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
function readURL(input){
	if (input.files && input.files[0]){
		var reader = new FileReader();
		reader.onload = function (e){
			$('#preview').attr('src',e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
function backToList(obj){ <%-- 자바스크립트 function --%>
	obj.action = "${contextPath}/board005/listArticles.do";
	obj.submit();

}
</script>
</head>
<body>
<h1 style = "text-align:center">새 글 쓰기</h1>
<form name ="articleForm" method ="post"
action = "${contextPath}/board005/addArticle.do"
enctype = "multipart/form-data"> <!-- 입력받는 값중에 파일형태가 존재함 -->

<table align ="center" border ="0">
<tr>
<td align ="right">글제목</td>
<td colspan ="2"><input type = "text" size="67" maxlength="500" name="title" /></td>
</tr>
<tr>
<td align ="right" valign ="top"><br>글 내용: </td>
<td colspan ="2"><textarea name = "content" rows ="10" cols ="65" maxlength ="4000"></textarea> </td>
</tr>
<tr>
<td align ="right">이미지 파일 첨부 :</td>
<td><input type="file" name ="imageFileName" onchange ="readURL(this);"/></td>
<td><img id="preview" src ="#" width=200 height =200 /></td>
</tr>
<tr>
<td align = "right"></td>
<td colspan ="2">
<input type ="submit" value ="글쓰기"/> <!-- 글쓰기 누르면 addArticle.do 호출 -->
<input type = button value ="목록보기" onclick ="backToList(this.form)"/> <!-- 목록보기 누르면 listArticles.do 호출 -->
</td>
</tr>
</table>
</form>
</body>
</html>