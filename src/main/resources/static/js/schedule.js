var nowDay;
var $tabs = $(".left-frame").tabs();

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


var drawSchedule = function(){
    var a = $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".spotimg");
    let html="";
    for(count=0;count<a.length;count++){
        var name = $(a[count]).parent().data().spotId;
        var x = a[count].getAttribute("src");
        const constHTML ="<div class='draw' data-spot-id='"+ name +"'>\n" +
            "                        <img src='"+ x +"'  class='spotimg'>\n" +
            "                    </div>\n" +
            "<img src='/image/right-arrow.png' class='arrowimg'>\n";
        html+=constHTML;
    }
    if(html!="")
        html=html+"<img src='/image/finish-flag.png' class='finish_flag'>\n";
    var day= $("#tab-frame>.ui-tabs-active>a").attr("href").substring(12);
    $(".drawSchedule").find(".day"+day+">.drawDay").html(html);
}

var drawdropSchedule = function(move){
    var a = $(move.attr("href")).find(".spotimg")
    let html="";
    for(count=0;count<a.length;count++){
        var name = $(a[count]).parent().data().spotId;
        var x = a[count].getAttribute("src");
        const constHTML ="<div class='draw' data-spot-id='"+ name +"'>\n" +
            "                        <img src='"+ x +"'  class='spotimg'>\n" +
            "                    </div>\n" +
            "<img src='/image/right-arrow.png' class='arrowimg'>\n";
        html+=constHTML;
    }
    if(html!="")
        html=html+"<img src='/image/finish-flag.png' class='finish_flag'>\n";
    var day= move.attr("href").substring(12)
    $(".drawSchedule").find(".day"+day+">.drawDay").html(html);

}

$("#spotSearch").on({
    click: function() {
        var dataValue = $("#searchKeyword").val();
        $.ajax({
            url: "/api/spots/search",
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
                	var addr = '';
                	if(spot.addr != null) {
                		addr += spot.addr;
                	}
                	if(spot.addrDtl != null) {
                		addr += spot.addrDtl;
                	}
                    const spotHTML = "<div class='details-spot' data-spot-id='"+ spot.id +"'>\n" +
                        "                <img src='" + spot.imgUrl[0] + "' class='spotimg'>\n" +
                        "                <dl class='spotInfo'>\n" +
                        "                    <dd class='category'>" + spot.category + "</dd>\n" +
                        "                    <dt class='title'>" + spot.name + "</dt>\n" +
                        "                    <dd class='like'><img src='/image/fullStar.png' alt=''/> <spn class='like-cnt'>" + spot.likeCnt + "</spn></dd>\n" +
                        "                </dl>" +
                        "			 	 <input type='hidden' class='addr' value='" + addr + "'/>" +
                        "            </div>";
                html += spotHTML;

            });
                $("#searchResult").html(html);
                $("#searchResult").sortable({
                    cursorAt : { left : 200, top : 150 },
                    connectWith: ".details-schedule",
                    placeholder: "ui-state-highlight",
                    start: function(e,ui){
                        ui.item.height(300);
                        ui.placeholder.height(ui.item.height());
                        $(".details_receipt").css("display","none");
                        $(".receiptimg").css("background-color","white");
                    },
                    change: function(e, ui) {
                        $(ui.placeholder).parent().find(".default-text").hide();
                    },
                    stop: function(e, ui) {
                        if ($("#searchResult").find("[data-spot-id='"+ $(ui.item).data().spotId +"']").length==0) {
                            $("#searchResult").append($(ui.item).clone());
                        }
                        $("#searchResult>.details-spot>.x_button").remove();
                        $("#searchResult>.details-spot>dl>.receiptimg").remove();
                        $("#searchResult>.details-spot>dl>.details_receipt").remove();
                        if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-spot").length==1){
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
                if (($("#scheduleday" + count)).length == 0) {            //해당 id가 미존재한다면
                    day_Form = "<div id='scheduleday" + count + "' class='day-Form day'>\n" +
                        "                  <div id='detailday"+count+"' class='details-schedule'>\n" +
                        "                 <div class='details-spot default-text'>원하는 장소를 드래그해서 가져오세요!</div>\n" +
                        "                 </div>\n" +
                        "            </div>";
                    $("#day-frame").append(day_Form);
                    tab_form = "<li class='tab' id='tab"+count+"'><a href='#scheduleday" + count + "'>"+ count +"일차</a></li>";
                    $("#tab-frame").append(tab_form);
                    draw_form="<div id='drawDay"+count+"' class='draw-form day"+count+"'>\n" +
                        "                <div class='nameDay'>"+count+" 일차</div>\n" +
                        "                <div class='drawDay'>\n" +
                        "                </div>\n" +
                        "            </div>";
                    $(".drawSchedule").append(draw_form);
                }
            }
            let save_button ="<button type='button' id='temp_save_button' class='save_button'>임시 저장</button>" +
                "<button type='button' id='complete_save_button' class='save_button'>저장하기</button>";
            $(".left").append(save_button);
            for(count;count<=nowDay;count++){
                if (($("#scheduleday" + count)).length != 0) {            //해당 id가 존재한 다면
                    $("#scheduleday"+count).remove();
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

        $(".details-schedule").sortable({
            cursorAt : { left : 200, top : 150 },
            placeholder: "ui-state-highlight",
            cancel: ".default-text,.details_receipt",
            start: function(e,ui){
                ui.item.height(300);
                ui.placeholder.height(ui.item.height());
                $(".details_receipt").css("display","none");
                $(".receiptimg").css("background-color","white");
            },
            beforeStop: function(e, ui) {
                drawSchedule();
            },
            receive: function(e, ui) {
                if ($(ui.item).parent().find("[data-spot-id='"+ $(ui.item).data().spotId +"']").length>1 && $(ui.item).parent().attr("class")=="details-schedule ui-sortable") {
                    alert("같은 날 같은 장소를 두번 갈 수 없습니다.");
                    ui.sender.sortable('cancel');
                }
                else if($(ui.item).parent().attr("id")!="searchResult"&& $(ui.item).find(".x_button").length==0) {
                    var imagetag = "<img src='/image/x_button.png' class='x_button'>";
                    var receipttag =
                        "<img src='/image/receipt.png' class='receiptimg'>" +
                        "<div class='details_receipt'>" +
                        "<img src='/image/plus_button.png' class='plus_button'>" +
                        "   <input type='text' class='default_receipt' value='이름(ex.식대) : 금액(단위:원)' readonly>" +
                        "</div>";

                    $(ui.item).prepend(imagetag);
                    $(ui.item).find(".spotInfo").append(receipttag);
                    drawSchedule();
                }
            }
        }).disableSelection();
        $(".ui-tabs-anchor").droppable({
            accept: ".details-schedule>.details-spot",
            hoverClass : "highlight",
            drop: function( event, ui ) {
                var $item = $(this);
                var $list = $($($item).attr("href")).find(".details-schedule");
                var itemid = $(".ui-sortable-helper").data().spotId;
                if($(".ui-state-active").attr("aria-labelledby")!=$($item).attr("id")){
                    if($($list).find("[data-spot-id='"+ itemid +"']").length!=0){
                        alert("같은 날 같은 장소를 두번 갈 수 없습니다.");
                    }
                    else {
                        ui.draggable.hide(function () {
                            ($(this)).appendTo($list).show("");
                            $($list).find(".default-text").hide();
                            drawSchedule();
                            drawdropSchedule(($item));
                        });
                        if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-spot").length==2){
                            $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".default-text").show();
                        }
                    }
                }
                $(".details_receipt").css("display","none");
                $(".receiptimg").css("background-color","white");
            }
        });
    }
});

$(document).on("click","#temp_save_button",function(){
    var message = confirm("현재까지의 스케줄을 임시 저장하시겠습니까?");
    if($($("#day-frame").find(".spotimg")[0]).attr("src")==null){
        var thumnail_url = "http://tour.jeonju.go.kr/planweb/upload/9be517a74f72e96b014f820463970068/inine/content/preview/31fcbcdc-6884-429c-9305-bf7e7b761b13.jpg.png";
    }
    else{
        var thumnail_url = $($("#day-frame").find(".spotimg")[0]).attr("src");
    }
    if(message==true){
        if($("#uuid").val() == "") {        //신규 생성
            var uuid = ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1) + ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1) + ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
            $("#uuid").val(uuid);
        }
        var budget_name = $(".left-frame").find(".budget_name");
        var budget_money=$(".left-frame").find(".budget_money");
        var money=[];
        for(var i=0;i<budget_name.length;i++){
            if(budget_name[i].value.trim().length>0&&budget_money[i].value.length>0){
                var detailspot = $(budget_money[i]).parent().parent().parent().parent();
                var spotid = $(budget_money[i]).parent().parent().parent().parent().data().spotId;
                var day = new Date($("#startDate").val());
                var sche_date =formatDate(new Date(day.setDate(day.getDate() + (detailspot.parent().attr("id").substring(9)*1)-1)));
                money.push({id : $("#uuid").val(), scheSpotId : spotid, name : budget_name[i].value, amount : budget_money[i].value, scheDate :sche_date});
            }
        }
        var list = $(".left-frame").find(".details-spot").not(".default-text");
        var seq = 0;
        var schespot=[];
        for(var i =0; i< list.length;i++){
            var id = $("#uuid").val();
            var day = new Date($("#startDate").val());
            var sche_date =formatDate(new Date(day.setDate(day.getDate() + ($(list[i].parentElement).attr("id").substring(9)*1)-1)));
            if(i>=1 && list[i].parentElement == list[i-1].parentElement){
                seq +=1;
            }
            else{
                seq =1;
            }
            var sequence = seq;
            var spotid = list[i].dataset.spotId;
            schespot.push({id : id, scheDate : sche_date, sequence : seq, spotId : spotid});
        }
            const addForm = {
                "id" : $("#uuid").val(),
                "title" : $("#schedule_title").val(),
                "startDate" : $("#startDate").val(),
                "endDate" : $("#endDate").val(),
                "isPublic" : $("select[name=isPublic]").val(),
                "thumnailUrl" : thumnail_url,
                "isWriting" : 0,
                "scheSpot" : schespot,
                "money" : money
             };
            $.ajax({
                url: "/api/schedules/"+$("#uuid").val(),
                method: "POST",
                data: JSON.stringify(addForm),
                contentType: "application/json",
                success: () => {
            },
                error: () => {
            }
        });


    }
});

$(document).on("click","#complete_save_button",function(){
    var message = confirm("스케줄 작성을 완료하시겠습니까?\n" +
        "완료 후에도 언제든지 수정이 가능합니다.");
    if($($("#day-frame").find(".spotimg")[0]).attr("src")==null){
        var thumnail_url = "http://tour.jeonju.go.kr/planweb/upload/9be517a74f72e96b014f820463970068/inine/content/preview/31fcbcdc-6884-429c-9305-bf7e7b761b13.jpg.png";
    }
    else{
        var thumnail_url = $($("#day-frame").find(".spotimg")[0]).attr("src");
    }
    if(message==true){
        if($("#uuid").val() == "") {        //신규 생성
            var uuid = ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1) + ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1) + ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
            $("#uuid").val(uuid);
        }
        var budget_name = $(".left-frame").find(".budget_name");
        var budget_money=$(".left-frame").find(".budget_money");
        var money=[];
        for(var i=0;i<budget_name.length;i++){
            if(budget_name[i].value.trim().length>0&&budget_money[i].value.length>0){
                var detailspot = $(budget_money[i]).parent().parent().parent().parent();
                var spotid = $(budget_money[i]).parent().parent().parent().parent().data().spotId;
                var day = new Date($("#startDate").val());
                var sche_date =formatDate(new Date(day.setDate(day.getDate() + (detailspot.parent().attr("id").substring(9)*1)-1)));
                money.push({id : $("#uuid").val(), scheSpotId : spotid, name : budget_name[i].value, amount : budget_money[i].value, scheDate :sche_date});
            }
        }
        var list = $(".left-frame").find(".details-spot").not(".default-text");
        var seq = 0;
        var schespot=[];
        for(var i =0; i< list.length;i++){
            var id = $("#uuid").val();
            var day = new Date($("#startDate").val());
            var sche_date =formatDate(new Date(day.setDate(day.getDate() + ($(list[i].parentElement).attr("id").substring(9)*1)-1)));
            if(i>=1 && list[i].parentElement == list[i-1].parentElement){
                seq +=1;
            }
            else{
                seq =1;
            }
            var sequence = seq;
            var spotid = list[i].dataset.spotId;
            schespot.push({id : id, scheDate : sche_date, sequence : seq, spotId : spotid});
        }
        const addForm = {
            "id" : $("#uuid").val(),
            "title" : $("#schedule_title").val(),
            "startDate" : $("#startDate").val(),
            "endDate" : $("#endDate").val(),
            "isPublic" : $("select[name=isPublic]").val(),
            "thumnailUrl" : thumnail_url,
            "isWriting" : 1,
            "scheSpot" : schespot,
            "money" : money
        };
        $.ajax({
            url: "/api/schedules/"+$("#uuid").val(),
            method: "POST",
            data: JSON.stringify(addForm),
            contentType: "application/json",
            success: () => {
            location.href = "/";
        },
            error: () => {
        }
    });


    }
});

$(document).on("click",".receiptimg",function(){
    if($(this).parent().find(".details_receipt").css("display")=="block") {
        $(this).parent().find(".details_receipt").css("display", "none");
        $(this).css("background-color","white");
    }
    else {
        $(this).parent().find(".details_receipt").css("display", "block");
        $(this).css("background-color","lightgray");
    }
});

$(document).on("click",".finish_flag",function(){
    var summary = $(this).parent().parent();
    var day = summary.attr("id").substring(7);
    var sumspot = summary.find(".draw").length;
    var summoney = $("#detailday"+day).find(".budget_money");
    var totalmoney=0;
    for(var i=0; i< summoney.length;i++){
        totalmoney+=Number(summoney[i].value);
    }

    totalmoney=totalmoney.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    var header = day+"일차 통계";
    var body = "총 여행 장소 : " + sumspot +"개 장소<br>" +
        "총 예상 경비 : " + totalmoney+"원";
    $("#spotName").html(header);
    $("#spotModal-body").html(body);
    $("#spotModal").modal("show");
});

$(document).on("click",".plus_button",function(){
    let a =$(this).parent().find(".budget").length;
    if(a>4){
        alert("한 장소당 추가가능한 예산은 최대 5개 입니다.");
    }
    else {
        const html = "<div class='budget'>" +
            "<input type='text' class='budget_name'>:" +
            "<input type='number' class='budget_money' min='0' step='500'>" +
            "<img src='/image/minus_button.png' class='minus_button'>" +
            "</div>";
        $(this).parent().append(html);
    }
});

$(document).on("click",".minus_button",function(){
    $(this).parent().remove();
});

$(document).on("keyup",".budget_name",function(e){
    if($(this).val().length>10){
        alert("제목은 10자 미만으로 적어주세요");
        $(this).val($(this).val().substring(0,10));
    }
});

$(document).on("keyup",".budget_money",function(e){
    if($(this).val().length>7){
        alert("한 주제에 대한 예산은 999만원을 넘길 수 없습니다.");
        $(this).val($(this).val().substring(0,7));
    }
});


$(document).on("click",".x_button",function(){
    $(this).parent().remove();
    drawSchedule();
    if($($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".details-spot").length==1){
        $($("#tab-frame>.ui-tabs-active>a").attr("href")).find(".default-text").show();
    }
});

$(document).on("click",".draw",function(){
    var day = $(this).parent().parent().attr("id").substring(7);
    var spotname = $(this).data().spotId;
    var spot = $("#detailday"+day).find("[data-spot-id='"+ spotname +"']");
    var head = '';
    head += spot.find(".title").text();
    var html='';
    html +="<img src='"+ $(this).find(".spotimg").attr("src")+"'  class='spotimg'>\n" +
        "                    <dd class='like'>\n" +
        "                        <img src='/image/fullStar.png'>\n" +
        "                        <spn class='like-cnt'>"+ spot.find(".like-cnt").text()+"</spn>\n" +
        "                    </dd>\n" +
        "    <img src='/image/receipt.png' class='receiptimg'>" +
        "<div class='details_receipt'>" +
        "   <input type='text' class='default_receipt' value='이름(ex.식대) : 금액(단위:원)' readonly>";
    let budget_name = spot.find(".budget_name");
    let budget_money = spot.find(".budget_money");
    for(var i =0; i< budget_name.length;i++)
    {
        if(budget_name[i].value.replace(/ /gi, "").length!=0&&budget_money[i].value.length!=0) {
            var receipt = "<div class='budget'>" +
                "<input type='text' class='budget_name' value='" + budget_name[i].value + "' readonly>:" +
                "<input type='number' class='budget_money' value='" + budget_money[i].value + "' readonly> " +
                "</div>"
            html += receipt;
        }
    }
    html += "</div>";
    $("#spotName").html(head);
    $("#spotModal-body").html(html);
    $("#spotModal").modal("show");
});





$(document).ready(function(){
    $_("#schedule-create-menu").style.color = "lightblue";

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

$("#spotModal-footer>.btn-secondary").on("click",function(){
    $("#spotModal").modal("hide");
});

$('#fromDate').datepicker({
    dateFormat: 'yy-mm-dd' //Input Display Format 변경
    ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
    ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
    ,changeYear: true //콤보박스에서 년 선택 가능
    ,changeMonth: true //콤보박스에서 월 선택 가능
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
    ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
    ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
    ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트
    ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
    ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
    ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
    ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
    ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
});

$(document).on("click",".arrowimg",function(){
	// finish 직전 화살표가 아닌 경우
	if($(this).next().prop('tagName') != "IMG") {
		var day = $(this).parent().parent().attr("id").substring(7);
		var startSpotId = $(this).prev().data().spotId;
		var endSpotId = $(this).next().data().spotId;
		var startSpotAddr = $("#detailday"+day).find("[data-spot-id='"+ endSpotId +"']").parent().find(".addr").val();
		var endSpotAddr = $("#detailday"+day).find("[data-spot-id='"+ endSpotId +"']").parent().find(".addr").val();
		var url = "http://map.daum.net/?sName=" + startSpotAddr + "&eName=" + endSpotAddr;
		window.open(url,'길찾기','location=no,status=no,scrollbars=yes');
	}
});
