
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    return [year, month, day].join('-');
}

$(document).on("click","#fix_button",function() {
    $.ajax({
        url: "/api/schedules/changewriting",
        method: "POST",
        data: {
            id: $("#uuid").val(),
        }, success: function () {
            window.location.reload();
        }
    });
});


$(document).on("click",".draw",function(){
    $(".draw").css("border","none");
    $(this).css("border","2px solid red");
    var day = new Date($("#startDate").val());

    var sche_date = formatDate(new Date(day.setDate(day.getDate() +$(this).parent().parent().attr("id").substring(7)*1-1)));
    $.ajax({
        url: "/api/schedules/schespot",
        method: "GET",
        data: {
            id: $("#uuid").val(),
            date : sche_date,
            spotId : $(this).data().spotId
        },
        success: function (data) {
            let html = '';
            var spot = data.spot;
            var money = data.money;
            html = "<div class='category'><h6>"+spot.category+"</h6></div>\n" +
                "    <div class='spotname'><h5>"+spot.name+"</h5></div>\n" +
                "    <img src='"+data.spot.imgUrl[0] +"' class='spotimg'>\n" +
                "    <div class='like'>\n" +
                "        <img src='/image/fullStar.png'>\n" +
                "        <div class='likeCnt'>"+spot.likeCnt+"</div>\n" +
                "    </div>\n" +
                "    <ul class='etcinfo'>\n" +
                "       <li class='addr'>"+spot.addr+" "+spot.addrDtl+"\n" +
                "       </li>\n" +
                "        <li class='tel'>"+spot.tel+"</li>\n" +
                "        <li class='url'><a href='"+spot.url+"'>"+spot.url+"</a></li>\n" +
                "    </ul>\n" +
                "    <div class='budget'>\n" +
                "        <div class='budget_title'><h6>예산</h6></div>\n"+
                "<div class='budgetset default_budget'>" +
                "        <div class='name '>이름</div>\n" +
                "        <div class='money '>금액(원)</div>\n" +
                "</div>";
            var sum=0;
            for(var i=0; i<money.length;i++) {
                html+="<div class='budgetset'>" +
                    "<div class='name'>"+money[i].name+"</div>\n" +
                    "<div class='money'>"+money[i].amount+"</div>\n" +
                    "</div>";
                sum=sum+(money[i].amount*1);
            }
            html+=" <div class='budget_result'><h6>총 "+ sum +"원</h6></div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class='instagram-container'>\n" +
                "        <h4 id='hashtag'><img src='/image/instagram.png' alt='' /> #"+spot.name+" </h4><span id='more-view'>더보기</span>\n" +
                "        <div class='hashtag-container'>\n" +
                "\n" +
                "        </div>\n" +
                "    </div>";
            html=html.replace(/null/gi,"");
            $(".detailInfo").html(html);


            const hashtag = spot.name.replace(" ", "");

            const url = "https://www.instagram.com/explore/tags/"+ hashtag +"/?__a=1";
            $.get(url, function(data, status) {
                let html = ``;
                for(let i = 0; i < 4; i++) {
                    if(i >= data.graphql.hashtag.edge_hashtag_to_media.edges.length) {
                        break;
                    }
                    const hashtagContent = data.graphql.hashtag.edge_hashtag_to_media.edges[i].node;
                    html += `<img class='hashtag-img' src='` + hashtagContent.thumbnail_resources[2].src + `'>`;
                }
                $(".hashtag-container").html(html);
            });

            $_("#more-view").addEventListener("click", () => { location.href = "https://www.instagram.com/explore/tags/"+hashtag+"/?hl=ko";  });
        }
    });

});

$(document).ready(function(){
    var toDate = $("#startDate").val();
    var fromDate = $("#endDate").val();
    var diff = (new Date(fromDate).getTime() - new Date(toDate).getTime()) / (1000*60*60*24)+1;
    var textday = "("+diff+"일)";
    $("#trapTerm").val(textday);

});
