function switchStarImg(img) {
	
	var imgCnt = img.parentElement.parentElement.parentElement.parentElement.children[1].children[2].children[1];
	
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
				imgCnt.innerHTML = data;
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

function getSpotDetail(div) {
	var id = div.dataset.id;
	if(div.getAttribute('class') == 'star')
		alert("dfdsfd");
	
	if(div.getAttribute('class') != 'star') {
		var form = document.createElement("form");
		var parm = new Array();
		var input = new Array();
	
		form.action = "/spots/detail";
		form.method = "POST";
		
		parm.push(['id', id]);
		
		for(var i=0; i<parm.length; i++) {
			input[i] = document.createElement("input");
			input[i].setAttribute("type", "hidden");
			input[i].setAttribute("name", parm[i][0]);
			input[i].setAttribute("value", parm[i][1]);
			
			form.appendChild(input[i]);
		}
		
		document.body.appendChild(form);
		form.submit();
	}
}