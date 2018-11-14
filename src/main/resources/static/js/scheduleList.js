function switchStarImg(img) {
    if(img.getAttribute("src") === '/image/star.png') {
        $.ajax({
            url: '/api/schedules/'+img.dataset.id+'/like',
            type: 'post',
            success: function(data) {
                img.setAttribute("src", '/image/fullStar.png');
                img.closest(".schedule").querySelector(".like-cnt").innerText = data;
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
            url: '/api/schedules/'+img.dataset.id+'/like',
            type: 'delete',
            success: function(data) {
                img.setAttribute("src", '/image/star.png');
                img.closest(".schedule").querySelector(".like-cnt").innerText = data;
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

document.addEventListener("DOMContentLoaded", () => {
   $(".schedule").on("click", (evt) => {
       const target = evt.target;
       if(target.className === 'star') {
           return;
       }
       location.href = "/schedules/" + target.closest(".schedule").dataset.scheduleId + "/detail";
   });
});