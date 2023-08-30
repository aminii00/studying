<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<title>전국 캠핑장 현황</title>
</head>
<body>
  <h1>전국 캠핑장 현황</h1>

  <!-- 지도를 표시할 div 입니다 -->
  <div id="map" style="width:100%;height:700px;"></div>
<!--  플랫폼 값 https://localhost:8080 -->
   <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=7a56ebb1c6ad3ff9da7c6b9a8286d236&libraries=clusterer"></script>
  <script>
  const lat = 36.3492506;
  const lng = 127.3776511;

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
        mapOption = { 
            center: new kakao.maps.LatLng(lat, lng), // 지도의 중심좌표 - 그린컴퓨터
            level: 14 // 지도의 확대 레벨
        };

    // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption); 

    // 마커 클러스터러를 생성합니다 
    var clusterer = new kakao.maps.MarkerClusterer({
      map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
      averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
      minLevel: 10 // 클러스터 할 최소 지도 레벨 
    });

    // 서버에서 가져오기 (관광공사 API)
    const  url = 'https://apis.data.go.kr/B551011/GoCamping/basedList?numOfRows=20&pageNo=1&MobileOS=ETC&MobileApp=testAPP&serviceKey=FSYGYSHOVs2%2BJnU1JYGsvu95uuPcadpsYhM84SzqBz17%2BOWXXCO4eHbhLP4efKKIz8PLD%3D&_type=json';

    fetch(url)
    .then(result => result.json())   // json 파일을 객체로 변환
    .then(json => {                     
      // console.log(json);              
      const data = json.response.body.items.item;  // 객체에서 실제 내용만 data로 저장   
      console.log(data);

    // 마커들을 모아놓을 변수
    var markers = [];

    for(let i = 0; i < data.length; i++) {  
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(data[i].mapY, data[i].mapX)
      });

      markers.push(marker);   // 마커를 배열에 추가합니다

      var infowindow = new kakao.maps.InfoWindow( {
          content : data[i].facltNm   // 인포윈도우에 표시할 내용
      });

      // 마커에 이벤트를 등록합니다
      // 이벤트 리스너로는 클로저를 만들어 등록합니다 
      // 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다

      // 마커에 마우스오버하면 makeOverListener() 실행
      kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));  
      // 마커에서 마우스아웃하면 makeOutListener() 실행
      kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));   
    }

    // 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
    function makeOverListener(map, marker, infowindow) {
        return function() {
            infowindow.open(map, marker);          
        };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
    function makeOutListener(infowindow) {
        return function() {
            infowindow.close();
        };
    }

    clusterer.addMarkers(markers);
  });  // fetch 끝
  </script>
</body>
</html>