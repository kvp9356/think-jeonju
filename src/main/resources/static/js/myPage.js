class MyPage {
    constructor() {
        //스케쥴, 장소 카드 그리기
        $_("#my-schedule").addEventListener("click", this.myScheduleClickHandler.bind(this));
        $_("#like-spot").addEventListener("click", this.likeSpotClickHandler.bind(this));
        $_("#like-schedule").addEventListener("click", this.likeScheduleClickHandler.bind(this));

        $_(".mypage-container-paging").addEventListener("click", this.pageMoveHandler.bind(this));

        //좋아요 추가&삭제
        $_(".mypage-container").addEventListener("click", this.likeBtnClickHandler);

        $.ajax({
            url: "/api/myPage/schedules/page/1",
            method: "GET",
            success: this.myScheduleCallback.bind(this)
        });

        //상세페이지 이동
        $_(".mypage-container").addEventListener("click", this.changePageHandler);
    }
    changePageHandler(evt) {
        const target = evt.target;
        //별인 경우, 제외
        if(target.className === "star") {
            return;
        }
        //내스케쥴, 좋아요 장소, 좋아요 스케줄이 아닌 경우, 제외
        if(target.closest(".my-spot") === null && target.closest(".my-schedule") === null) {
            return;
        }

        //장소 상세페이지
        if(target.closest(".my-spot") !== null) {
            location.href = "/spots/" + target.closest(".my-spot").dataset.spotId + "/detail";
            return;
        }

        //스케쥴 상세페이지
        if(target.closest(".my-schedule") !== null) {
            location.href = "/schedules/" + target.closest(".my-schedule").dataset.scheduleId + "/detail";
            return;
        }
    }
    insertPageNumbers(paging) {
        let pageHTML = ``;
        if(paging.blockStart != 1) {
            pageHTML += `<li class="page-item"> <span  class="page-link" data-page='`+(paging.blockStart - 1)+`'> &laquo; </span></li>`;
        }
        for(let i = paging.blockStart; i <= paging.blockEnd; i++) {
            if(paging.currentBlock == i) {
                pageHTML += `<li class="active page-item"><span  class="page-link" data-page="` + i + `">` + i + `</span></li>`;
            } else {
                pageHTML += `<li class="page-item"><span  class="page-link" data-page="` + i + `">` + i + `</span></li>`;
            }
        }
        if(paging.blockEnd < paging.lastPage) {
            pageHTML += `<li class="page-item"> <span  class="page-link" data-page='`+(paging.blockEnd + 1)+`'> &raquo; </span></li>`;
        }
        $_(".mypage-container-paging .pagination").insertAdjacentHTML("beforeend", pageHTML);
    }
    pageMoveHandler(evt) {
        const target = evt.target;
        const destination = $_(".mypage-menu").querySelector(".in").id;

        if(!target.classList.contains("page-link")) {
            return;
        }

        if(target.parentElement.classList.contains("active")) {
            return;
        }

        if(destination === "my-schedule") {
            this.myScheduleAjax(target.dataset.page);
        } else if(destination === "like-spot") {
            this.likeSpotAjax(target.dataset.page);
        } else if(destination === "like-schedule") {
            this.likeScheduleAjax(target.dataset.page);
        }
    }
    likeBtnClickHandler(evt) {
        if(!evt.target.classList.contains("star")) {
            return;
        }
        const img = evt.target;
        
        //schedule과 spot에 따라 url 나뉨
        let url;
        if(img.closest(".my-spot") !== null) {
            url = '/api/spots/'+img.dataset.id+"/spotLike";
        } else if(img.closest(".my-schedule") !== null) {
            url = '/api/schedules/'+img.dataset.id+"/like";
        }

        if(img.getAttribute("src") === '/image/star.png') {
            $.ajax({
                url: url,
                type: 'post',
                success: function(data) {
                    img.setAttribute("src", '/image/fullStar.png');
                    img.closest(".my-content").querySelector(".like-cnt").innerText = data;
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    console.log("Status: " + textStatus);
                    console.log("Error: " + errorThrown);
                }
            });
        } else {
            $.ajax({
                url: url,
                type: 'delete',
                success: function(data) {
                    img.setAttribute("src", '/image/star.png');
                    img.closest(".my-content").querySelector(".like-cnt").innerText = data;
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    console.log("Status: " + textStatus);
                    console.log("Error: " + errorThrown);
                }
            });
        }
    }
    insertLikeScheduleHTML(schedule) {
        const scheduleHTML = `
            <div class='my-schedule my-content' data-schedule-id='{id}'>
                <img src="{imgUrl}" class="my-schedule-img my-content-img" onerror="this.src='/image/defaultThumnail.jpg'"/>
                <div class="star-container">
                    <img src="/image/fullStar.png" alt="" class="star" data-id="{id}"/>
                </div>
                <dl class="my-schedule-info my-content-info">
                    <dt class="title">{title}</dt>
                    <dd class="date">{date}일</dd>
                    <dd class="like"><img src="/image/fullStar.png" alt=""/> <spn class="like-cnt">{like}</spn></dd>
                </dl>
            </div>`;
        return scheduleHTML.replace(/{id}/g, schedule.id)
            .replace(/{imgUrl}/g, schedule.thumnailUrl)
            .replace(/{title}/g, schedule.title)
            .replace(/{date}/g, (new Date(schedule.endDate) - new Date(schedule.startDate)) / 86400000 + 1)
            .replace(/{like}/g, schedule.like);
    }
    likeScheduleCallback(data) {
        if(data.results.length === 0) {
            return;
        }
        let html = '';
        data.results.forEach(schedule => {
            html += this.insertLikeScheduleHTML(schedule);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);

        $(".pagination").empty();
        this.insertPageNumbers(data.paging);
    }
    likeScheduleClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.likeScheduleAjax(1);
    }
    insertLikeSpotHTML(spot) {
        const spotHTML = `
            <div class='my-spot my-content' data-spot-id='{id}'>
                <img src="{imgUrl}" class="my-spot-img my-content-img"/>
                <div class="star-container">
                    <img src="/image/fullStar.png" alt="" class="star" data-id="{id}"/>
                </div>
                <dl class="my-spot-info my-content-info">
                    <dd class="category">{category}</dd>
                    <dt class="title">{title}</dt>
                    <dd class="like"><img src="/image/fullStar.png" alt=""/> <spn class="like-cnt">{like}</spn></dd>
                </dl>
            </div>`;
        return spotHTML.replace(/{id}/g, spot.id)
            .replace(/{imgUrl}/g, spot.imgUrl[0])
            .replace(/{category}/g, spot.category)
            .replace(/{title}/g, spot.name)
            .replace(/{like}/g, spot.likeCnt);
    }
    likeSpotCallback(data) {
        if(data.results.length === 0) {
            return;
        }
        let html = '';
        data.results.forEach(spot => {
            html += this.insertLikeSpotHTML(spot);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);

        $(".pagination").empty();
        this.insertPageNumbers(data.paging);
    }
    likeSpotClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.likeSpotAjax(1);
    }
    insertMyScheduleHTML(schedule) {
        const scheduleHTML = `
            <div class='my-schedule my-content' data-schedule-id='{id}'>
                <img src="{imgUrl}" class="my-schedule-img my-content-img" onerror="this.src='/image/defaultThumnail.jpg'"/>
                <dl class="my-schedule-info my-content-info">
                    <dt class="title">{title}</dt>
                    <dd class="date">{date}일 ({startDate} ~ {endDate})</dd>
                    <dd class="like"><img src="/image/fullStar.png" alt=""> <spn class="like-cnt">{like}</spn></dd>
                </dl>
            </div>`;
        return scheduleHTML.replace(/{id}/g, schedule.id)
            .replace(/{imgUrl}/g, schedule.thumnailUrl)
            .replace(/{title}/g, schedule.title)
            .replace(/{date}/g, (new Date(schedule.endDate) - new Date(schedule.startDate)) / 86400000 + 1)
            .replace(/{startDate}/g, schedule.startDate)
            .replace(/{endDate}/g, schedule.endDate)
            .replace(/{like}/g, schedule.like);
    }
    myScheduleCallback(data) {
        if(data.results.length === 0) {
            return;
        }
        let html = '';
        data.results.forEach(schedule => {
            html += this.insertMyScheduleHTML(schedule);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);

        $(".pagination").empty();
        this.insertPageNumbers(data.paging);
    }
    myScheduleClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.myScheduleAjax(1);
    }
    likeScheduleAjax(pageNo) {
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/schedules/like/page/"+pageNo,
            method: "GET",
            success: this.likeScheduleCallback.bind(this)
        });
    }
    likeSpotAjax(pageNo) {
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/spots/like/page/"+pageNo,
            method: "GET",
            success: this.likeSpotCallback.bind(this)
        });
    }
    myScheduleAjax(pageNo) {
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/schedules/page/"+pageNo,
            method: "GET",
            success: this.myScheduleCallback.bind(this)
        });
    }
    clearMyPageContainer() {
        $(".mypage-container").empty();
    }
    changeFocusMenu(target) {
        if(!target.classList.contains("in")) {
            $(".mypage-menu a").removeClass("in");
        }
        target.classList.add("in");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new MyPage();
    $_("#my-page").style.color = "lightblue";
});