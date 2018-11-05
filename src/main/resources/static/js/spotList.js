function switchStarImg(img) {
	
	var imgCnt = img.parentElement.parentElement.parentElement.parentElement.children[1].children[2].children[1];
	console.log(imgCnt);
	if(img.getAttribute("src") === '/image/star.png') {
		$.ajax({
			url: '/api/spots/'+img.dataset.id+"/spotLike",
			type: 'post',
			success: function(data) {
				img.setAttribute("src", '/image/fullStar.png');
				imgCnt.innerHTML = data;
			},
			statusCode: {
				403: function () {
					location.href = "/error";
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
				imgCnt.innerHTML = data;
			},
            statusCode: {
                403: function () {
                    location.href = "/error";
                }
            },
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				console.log("Status: " + textStatus); 
				console.log("Error: " + errorThrown); 
			}
		});
	}
	
}