<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>API���� �ڷ� ��������</title>
</head>
<body>
  <script>
    // �������� ��������
    var  url = 'http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/basedList?ServiceKey=HlkkfCPrQ8k0NTwqeg3zU7a0A6pPYXq%2FVn4YAED70eh1QoEjqZpGuy3Wbx8ihzA%2F4xL%2BBTbic%2BwQfX4pMiJM1w%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json';

    fetch(url)
    .then(result => result.json())   // json ������ ��ü�� ��ȯ
    .then(json => {                     
      // console.log(json);  
      const data = json.response.body.items.item;  // ��ü���� ���� ���븸 data�� ����   
      console.log(data);            
    });
  </script>
</body>
</html>