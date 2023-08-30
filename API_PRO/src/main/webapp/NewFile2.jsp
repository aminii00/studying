<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>API에서 자료 가져오기</title>
</head>
<body>
  <script>
    // 서버에서 가져오기
    var  url = 'http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/basedList?ServiceKey=HlkkfCPrQ8k0NTwqeg3zU7a0A6pPYXq%2FVn4YAED70eh1QoEjqZpGuy3Wbx8ihzA%2F4xL%2BBTbic%2BwQfX4pMiJM1w%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json';

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