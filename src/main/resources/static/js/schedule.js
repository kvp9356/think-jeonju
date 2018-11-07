var nowDay;
var $tabs = $(".left-frame").tabs();

var drawSchedule = function(){
    var a = $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".spotimg");
    let html="";
    for(count=0;count<a.length;count++){
        var name = $(a[count]).parent().find(".spotId").attr("class").split(' ')[0]
        var x = a[count].getAttribute("src");
        const constHTML ="<div class=\"draw\">\n" +
            "                        <img src=\""+ x +"\"  class=\"spotimg\">\n" +
            "                        <div class=\""+ name +" spotId\"></div>\n" +
            "                    </div>\n" +
            "<img src=\"/image/right-arrow.png\" class=\"arrowimg\">\n";
        html+=constHTML;
    }
    if(html!="")
        html=html+"<img src=\"/image/finish-flag.png\" class=\"arrowimg\">\n";
    var day= $("#tab-frame>.ui-tabs-active>a").attr("href").substring(12);
    $(".drawSchedule").find(".day"+day+">.drawDay").html(html);
}

var drawdropSchedule = function(move){
    var a = $(move.attr("href")).find(".spotimg")
    let html="";
    for(count=0;count<a.length;count++){
        var name = $(a[count]).parent().find(".spotId").attr("class").split(' ')[0]
        var x = a[count].getAttribute("src");
        const constHTML ="<div class=\"draw\">\n" +
            "                        <img src=\""+ x +"\"  class=\"spotimg\">\n" +
            "                        <div class=\""+ name +"\"></div>\n" +
            "                    </div>\n" +
            "<img src=\"/image/right-arrow.png\" class=\"arrowimg\">\n";
        html+=constHTML;
    }
    if(html!="")
        html=html+"<img src=\"/image/finish-flag.png\" class=\"arrowimg\">\n";
    var day= move.attr("href").substring(12)
    $(".drawSchedule").find(".day"+day+">.drawDay").html(html);

}

$("#spotSearch").on({
    click: function() {
        var dataValue = $("#searchKeyword").val();
        $.ajax({
            url: "/schedule/search",
            method: "POST",
            data: {
                dataValue : dataValue
            },
            success: function (spots) {
                let html ='';
                if(spots.length === 0) {
                    return;
                }
                $("#searchResult").html(html);
                spots.forEach(spot => {
                    const spotHTML = "<div class=\"details-Spot\">\n" +
                        "                <div class=\"" + spot.id + " spotId\"></div>" +
                        "                <img src=\"" + spot.imgUrl[0] + "\" class=\"spotimg\">\n" +
                        "                <dl class=\"spotInfo\">\n" +
                        "                    <dd class=\"category\">" + spot.category + "</dd>\n" +
                        "                    <dt class=\"title\">" + spot.name + "</dt>\n" +
                        "                    <dd class=\"like\"><img src=\"/image/fullStar.png\" alt=\"\"/> <spn class=\"like-cnt\">" + spot.likeCnt + "</spn></dd>\n" +
                        "                </dl>" +
                        "            </div>";
                html += spotHTML;

            });
                $("#searchResult").html(html);
                $("#searchResult").sortable({
                    cursorAt : { left : 200, top : 150 },
                    connectWith: ".details-Schedule",
                    placeholder: "ui-state-highlight",
                    start: function(e,ui){
                        ui.placeholder.height(ui.item.height());
                    },
                    change: function(e, ui) {
                        $(ui.placeholder).parent().find(".default-text").hide();
                    },
                    stop: function(e, ui) {
                        if ($("#searchResult").find("."+$(ui.item).find(".spotId").attr("class").replace(' ',".")).length==0) {
                            $("#searchResult").append($(ui.item).clone());
                        }
                        $("#searchResult>.details-Spot>.x_button").remove();
                        $("#searchResult>.details-Spot>.budget").remove();
                        if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-Spot").length==1){
                            $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".default-text").show();
                        }
                    }
                }).disableSelection();
            }
        });
    }
});

$(".btn-primary").on({
    click: function() {
        toDate = $('#toDate').val();
        fromDate = $('#fromDate').val();
        schedule_title = $('#schedule-title').val();
        if(schedule_title.replace(/ /gi, "").length==0){
            alert("제목을 다시 한번 확인해 주세요.");
            return;
        }
        else if(schedule_title.length>15){
            alert("제목의 길이는 최대 15자입니다.");
            return;
        }


        var date_pattern = /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/;

        if(!date_pattern.test(fromDate) || !date_pattern.test(toDate)){
            alert("날짜 형식을 확인해 주세요(ex: 2018-11-03)");
            return;
        }

        if(new Date(fromDate)>=new Date(toDate) && (new Date(toDate).getTime() - new Date().getTime()) / (1000*60*60*24) > -1 ) {
            document.querySelector('#schedule_title').value = schedule_title;
            document.querySelector('#startDate').value = toDate;
            document.querySelector('#endDate').value = fromDate;
            $("#myModal").modal('hide');
            var day = (new Date(fromDate).getTime() - new Date(toDate).getTime()) / (1000*60*60*24)+1;
            var textday = "(총 "+day+"일)";
            $("#trapTerm").val(textday)
            var count = 1;
            var day_Form;
            var tab_form;
            var draw_form;
            for(count=1;count<=day;count++) {
                if (($("#day" + count)).length == 0) {            //해당 id가 미존재한다면
                    day_Form = "<div id=\"scheduleday" + count + "\" class=\"day-Form day\">\n" +
                        "                  <div id=\"detailday"+count+"\" class=\"details-Schedule\">\n" +
                        "                 <div class=\"details-Spot default-text\">원하는 장소를 드래그해서 가져오세요!</div>\n" +
                        "                 </div>\n" +
                        "            </div>";
                    $("#day-frame").append(day_Form);
                    tab_form = "<li class=\"tab\" id=\"tab"+count+"\"><a href=\"#scheduleday" + count + "\">"+ count +"일차</a></li>";
                    $("#tab-frame").append(tab_form);
                    draw_form="<div id=\"drawDay"+count+"\" class=\"draw-form day"+count+"\">\n" +
                        "                <div class=\"nameDay\">"+count+" 일차</div>\n" +
                        "                <div class=\"drawDay\">\n" +
                        "                </div>\n" +
                        "            </div>";
                    $(".drawSchedule").append(draw_form);
                }
            }
            for(count;count<=nowDay;count++){
                if (($("#day" + count)).length != 0) {            //해당 id가 존재한 다면
                    $("#day"+count).remove();
                    $("#tab"+count).remove()
                    $("#drawDay"+count).remove();
                }
            }
            nowDay = day;

        }
        else{
            if(new Date(toDate) < new Date()){
                alert("출발 날짜는 오늘 보다 이후여야 합니다. 다시 한번 확인해주세요.");
            }
            else{
                alert("도착 날짜가 출발 날짜보다 작습니다. 다시 한번 확인해주세요.")
            }
        }
        $(".left-frame").tabs("refresh");
        $(".left-frame").tabs({
            active : 0
        });

        $(".details-Schedule").sortable({
            cursorAt : { left : 200, top : 150 },
            placeholder: "ui-state-highlight",
            cancel: ".default-text,input",
            start: function(e,ui){
                ui.placeholder.height(ui.item.height());
            },
            beforeStop: function(e, ui) {
                drawSchedule();
            },
            receive: function(e, ui) {
                if ($(ui.item).parent().find("."+$(ui.item).find(".spotId").attr("class").replace(' ',".")).length>1 && $(ui.item).parent().attr("class")=="details-Schedule ui-sortable") {
                    alert("같은 날 같은 장소를 두번 갈 수 없습니다.");
                    ui.sender.sortable('cancel');
                }
                else if($(ui.item).parent().attr("id")!="searchResult"&& $(ui.item).find(".x_button").length==0) {
                    var imagetag = "<img src=\"/image/x_button.png\" class=\"x_button\">";
                    var budgettag = "<div class=\"budget\">\n예산(단위:원):" +
                        "<input type=\"number\" class=\"details-budget\" min=\"0\" step=\"500\" numberOnly>\n" +
                        "</div>"
                    $(ui.item).prepend(imagetag);
                    $(ui.item).find(".spotInfo").append(budgettag);
                    drawSchedule();
                }
            }
        }).disableSelection();
        $(".ui-tabs-anchor").droppable({
            accept: ".details-Schedule>.details-Spot",
            hoverClass : "highlight",
            drop: function( event, ui ) {
                var $item = $(this);
                var $list = $($($item).attr("href")).find(".details-Schedule");
                var itemid = $(".ui-sortable-helper").find(".spotId").attr("class").replace(' ',".");
                if($(".ui-state-active").attr("aria-labelledby")!=$($item).attr("id")){
                    if($($list).find("."+itemid).length!=0){
                        alert("같은 날 같은 장소를 두번 갈 수 없습니다.");
                    }
                    else {
                        ui.draggable.hide(function () {
                            ($(this)).appendTo($list).show("");
                            $($list).find(".default-text").hide();
                            drawSchedule();
                            drawdropSchedule(($item));
                        });
                        if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-Spot").length==2){
                            $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".default-text").show();
                        }
                    }
                }
            }
        });
    }
});


$(document).on("click",".x_button",function(){
    $(this).parent().remove();
    drawSchedule();
    if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-Spot").length==1){
        $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".default-text").show();
    }
});

$(document).on("click",".draw",function(){
    var day = $(this).parent().parent().attr("id").substring(7);
    var spotname = $(this).find(".spotId").attr("class").split(' ')[0];
    var spot = $("#detailday"+day).find("."+spotname).parent();
    var head = '';
    head += spot.find(".title").text();
    var html='';
    html +="<img src=\""+ $(this).find(".spotimg").attr("src")+"\"  class=\"spotimg\">\n" +
        "                    <dd class=\"like\">\n" +
        "                        <img src=\"/image/fullStar.png\">\n" +
        "                        <spn class=\"like-cnt\">"+ spot.find(".like-cnt").text()+"</spn>\n" +
        "                    </dd>\n" +
        "                    <div class=\"budget\">\n" +
        "                        예산(단위:원):<input type=\"text\" class=\"details-budget\" readonly value=\""+ spot.find(".details-budget").val() +"\">\n" +
        "                    </div>";
    $("#spotName").html(head);
    $("#spotModal-body").html(html);
    $("#spotModal").modal("show");
});



$(document).ready(function(){
    $('#myModal').modal('show');
    $("#myModal").on("show.bs.modal", function (event) {
        var modal = $(this);
        modal.find(".modal-header>.modal-title>#schedule-title").val($("#schedule_title").val());
        modal.find(".modal-body>#modalForm>#toDate").val($("#startDate").val());
        modal.find(".modal-body>#modalForm>#fromDate").val($("#endDate").val());
    });
    $("#myModal").on("hide.bs.modal", function (event) {
        var modal = $(this);
        modal.find(".modal-header>.modal-title>#schedule-title").val("");
        modal.find(".modal-body>#modalForm>#toDate").val("");
        modal.find(".modal-body>#modalForm>#fromDate").val("");
    });

});

$('#fromDate').datepicker({
    dateFormat: 'yy-mm-dd' //Input Display Format 변경
    ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
    ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
    ,changeYear: true //콤보박스에서 년 선택 가능
    ,changeMonth: true //콤보박스에서 월 선택 가능
//    ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
    ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
    ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
    ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트
    ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
    ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
    ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
    ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
    ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
});

$('#toDate').datepicker({
    dateFormat: 'yy-mm-dd' //Input Display Format 변경
    ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
    ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
    ,changeYear: true //콤보박스에서 년 선택 가능
    ,changeMonth: true //콤보박스에서 월 선택 가능
//    ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
    ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
    ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
    ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트
    ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
    ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
    ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
    ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
    ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
});
