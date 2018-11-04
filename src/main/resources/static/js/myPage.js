class MyPage {
    constructor() {
        //스케쥴, 장소 카드 그리기
        $_("#my-schedule").addEventListener("click", this.myScheduleClickHandler.bind(this));
        $_("#like-spot").addEventListener("click", this.likeSpotClickHandler.bind(this));
        $_("#like-schedule").addEventListener("click", this.likeScheduleClickHandler.bind(this));

        //좋아요 추가&삭제
        $_(".mypage-container").addEventListener("click", this.likeBtnClickHandler);

        $.ajax({
            url: "/api/myPage/schedules",
            method: "GET",
            success: this.myScheduleCallback.bind(this)
        });
    }
    likeBtnClickHandler(evt) {
        if(!evt.target.classList.contains("star")) {
            return;
        }
        const img = evt.target;
        if(img.getAttribute("src") === '/image/star.png') {
            $.ajax({
                url: '/api/spots/'+img.dataset.id+"/spotLike",
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
                url: '/api/spots/'+img.dataset.id+"/spotLike",
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
            <div class='my-schedule my-content' data-scehdule-id='{id}'>
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
    likeScheduleCallback(schedules) {
        if(schedules.length === 0) {
            return;
        }
        let html = '';
        schedules.forEach(schedule => {
            html += this.insertLikeScheduleHTML(schedule);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);
    }
    likeScheduleClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/schedules/like",
            method: "GET",
            success: this.likeScheduleCallback.bind(this)
        });
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
    likeSpotCallback(spots) {
        if(spots.length === 0) {
            return;
        }
        let html = '';
        spots.forEach(spot => {
            html += this.insertLikeSpotHTML(spot);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);
    }
    likeSpotClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/spots/like",
            method: "GET",
            success: this.likeSpotCallback.bind(this)
        });
    }
    insertMyScheduleHTML(schedule) {
        const scheduleHTML = `
            <div class='my-schedule my-content' data-scehdule-id='{id}'>
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
    myScheduleCallback(schedules) {
        if(schedules.length === 0) {
            return;
        }
        let html = '';
        schedules.forEach(schedule => {
            html += this.insertMyScheduleHTML(schedule);
        });
        $_(".mypage-container").insertAdjacentHTML("beforeend", html);
    }
    myScheduleClickHandler(evt) {
        if(evt.currentTarget.classList.contains("in")) {
            return;
        }
        this.changeFocusMenu(evt.currentTarget);
        this.clearMyPageContainer();

        $.ajax({
            url: "/api/myPage/schedules",
            method: "GET",
            success: this.myScheduleCallback.bind(this)
        });
    }
    clearMyPageContainer() {
        //todo: mypage-container 내용 지우기
        $(".mypage-container").empty();
    }
    changeFocusMenu(target) {
        if(!target.classList.contains("in")) {
            $(".mypage-menu a").removeClass("in");
        }
        target.classList.add("in");
    }
}

document.addEventListener("DOMContentLoaded", new MyPage());