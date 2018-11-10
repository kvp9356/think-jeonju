var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
  showDivs(slideIndex += n);
}

function currentDiv(n) {
  showDivs(slideIndex = n+1);
}

function showDivs(n) {
  var i;
  var x = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("demo");
  if (n > x.length) {slideIndex = 1}    
  if (n < 1) {slideIndex = x.length}
  for (i = 0; i < x.length; i++) {
     x[i].style.display = "none";  
  }
  for (i = 0; i < dots.length; i++) {
     dots[i].className = dots[i].className.replace(" w3-white", "");
  }
  x[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " w3-white";
}
function getMapSearch() {
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = {
	    center: new daum.maps.LatLng(35, 127), // 지도의 중심좌표
	    level: 3 // 지도의 확대 레벨
	};  

	//지도를 생성합니다    
	var map = new daum.maps.Map(mapContainer, mapOption); 

	//주소-좌표 변환 객체를 생성합니다
	var geocoder = new daum.maps.services.Geocoder();

	//주소로 좌표를 검색합니다
	geocoder.addressSearch($("#addr").val(), function(result, status) {

		// 정상적으로 검색이 완료됐으면 
		 if (status === daum.maps.services.Status.OK) {
		
		    var coords = new daum.maps.LatLng(result[0].y, result[0].x);
		
		    // 결과값으로 받은 위치를 마커로 표시합니다
		    var marker = new daum.maps.Marker({
		        map: map,
		        position: coords
		    });
		
		    // 인포윈도우로 장소에 대한 설명을 표시합니다
		    var infowindow = new daum.maps.InfoWindow({
		        content: '<div style="min-width:max-content;text-align:center;padding:6px 0;">'+ $("#spotName").val() +'</div>'
		    });
		    infowindow.open(map, marker);
		
		    // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
		    map.setCenter(coords);
		} 
	});  
	document.getElementById('map').scrollIntoView();
}

function switchStarImg(img) {
	
	if(img.getAttribute("src") === '/image/star.png') {
		$.ajax({
			url: '/api/spots/'+img.dataset.id+"/spotLike",
			type: 'post',
			success: function(data) {
				img.setAttribute("src", '/image/fullStar.png');
				$("#likeCnt").text(data);
			},
			statusCode: {
				403: function () {
					location.href = "/loginError";
                }
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				console.log("Status: " + textStatus); 
				console.log("Error: " + errorThrown); 
			}
		});
	} else {
		$.ajax({
			url: '/api/spots/'+img.dataset.id+"/spotLike",
			type: 'delete',
			success: function(data) {
				img.setAttribute("src", '/image/star.png');
				$("#likeCnt").text(data);
			},
            statusCode: {
                403: function () {
                    location.href = "/loginError";
                }
            },
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				console.log("Status: " + textStatus); 
				console.log("Error: " + errorThrown); 
			}
		});
	}	
}
var category = $("#category").text().trim();
var a = document.getElementsByClassName("searchMenu");

for(var i=0; i<a.length; i++) {
	if(a[i].innerHTML == category) {
		a[i].style.color = "black";
	}
} 