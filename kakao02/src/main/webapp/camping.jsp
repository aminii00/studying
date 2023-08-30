<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>API에서 자료 가져오기</title>
</head>
<body>
  <script>
    // 서버에서 가져오기
    var  url = 'http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/basedList?ServiceKey=FSYGYSHOVs2%2BJnU1JYGsvu95uuPcadpsYhM84SBOWXXCO4eHbhLP4efKKIz8PLbyO6tf93i5ajseadFg%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json';

    fetch(url)
    .then(result => result.json())   // json 파일을 객체로 변환
    .then(json => {                     
      // console.log(json);  
      const data = json.response.body.items.item;  // 객체에서 실제 내용만 data로 저장   
      console.log(data);            
    });
  </script>
</body>
</html>