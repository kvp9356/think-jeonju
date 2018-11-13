document.addEventListener("DOMContentLoaded", () => {
    $("#login").on("click", () => {
        showModal("login");
    });
    $("#join").on("click", () => {
        showModal("join");
    });

    $("#login-modal").on("hidden.bs.modal", () => {
        clearModal("login");
    });
    $("#join-modal").on("hidden.bs.modal", () => {
        clearModal("join");
    });


    $("#login-form-btn").on("click", () => {
        hideModal("join");
        showModal("login");
    });
    $("#join-form-btn").on("click", () => {
        hideModal("login");
        showModal("join");
    });


    $("#logout").on("click", logout);

    $("#spot-menu").on("click", () => {
        const form = document.createElement("form");
        let parm = new Array();
        let input = new Array();

        form.action = "/spots/All/search";
        form.method = "POST";

        document.body.appendChild(form);
        form.submit();
    });

    $("#login").removeAttr("href");
    $("#join").removeAttr("href");
    $("#logout").removeAttr("href");

    if($(".best-container").length > 0) {
        $(".best-container").click(changePageHandler);
    }
});

function changePageHandler(evt) {
    const target = evt.target;

    //장소, 스케쥴 카드가 아닌 경우, 제외
    if(target.closest(".best-content") === null) {
        return;
    }

    //베스트 장소 상세 페이지
    if(target.closest(".best-spot") !== null) {
        location.href = "/spots/" + target.closest(".best-spot").dataset.spotId + "/detail";
        return;
    }

    //베스트 스케쥴 상세 페이지
    if(target.closest(".best-schedule") !== null) {
        location.href = "/schedules/" + target.closest(".best-schedule").dataset.scheduleId + "/detail";
        return;
    }
}

function searchByKeyword() {
	var searchKeyword = $("#searchKeyword").val().trim();
	
	if(searchKeyword.length == 0) {
		alert("검색어를 입력하세요!");
	} else if(searchKeyword.length > 10) {
		alert("검색어의 길이는 10이하입니다.");
	} else {
		var form = document.createElement("form");
		var parm = new Array();
		var input = new Array();
		
		form.action = "/spots/All/search";
		form.method = "post";
		
		parm.push(['dataValue', searchKeyword]);
		
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