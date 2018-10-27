function $_(selector) {
    return document.querySelector(selector);
}

function showModal(sort) {
    $("#"+sort+"-modal").modal("show");
}

function hideModal(sort) {
    $("#"+sort+"-modal").modal("hide");
}

function clearModal(sort) {
    $("#"+sort+"-modal").find("input").val('').end();
    $("#"+sort+"-modal").find(".validation-error").css("display", "none");
}

function reloadHome() {
    location.href = "/";
}

function logout() {
    $.ajax({
        url: "/api/members/logout",
        method: "POST",
        success: () => {
            reloadHome();
        },
        error: () => {
            reloadHome();
        }
    })
}

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
        clearModal("login");
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

    $("#login").removeAttr("href");
    $("#join").removeAttr("href");
    $("#logout").removeAttr("href");
});