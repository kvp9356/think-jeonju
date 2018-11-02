class MyPage {
    constructor() {
        $_("#my-schedule").addEventListener("click", this.myScheduleClickHandler.bind(this));
        $_("#like-spot").addEventListener("click", this.likeSpotClickHandler.bind(this));
        $_("#like-schedule").addEventListener("click", this.likeScheduleClickHandler.bind(this));

        $.ajax({
            url: "/api/myPage/schedules",
            method: "GET",
            success: this.myScheduleCallback.bind(this)
        });
    }
    insertLikeScheduleHTML(schedule) {
        const scheduleHTML = `
            <div class='my-schedule' data-scehdule-id='{id}'>
                <img src="{imgUrl}" class="my-schedule-img" onerror="this.src='/image/defaultThumnail.jpg'"/>
                <dl class="my-schedule-info">
                    <dt class="title">{title}</dt>
                    <dd class="date">{date}일</dd>
                    <dd clss="like">좋아요 {like}</dd>
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
            <div class='my-schedule' data-scehdule-id='{id}'>
                <img src="{imgUrl}" class="my-schedule-img" onerror="this.src='/image/defaultThumnail.jpg'"/>
                <dl class="my-schedule-info">
                    <dt class="title">{title}</dt>
                    <dd class="date">{date}일 ({startDate} ~ {endDate})</dd>
                    <dd clss="like">좋아요 {like}</dd>
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