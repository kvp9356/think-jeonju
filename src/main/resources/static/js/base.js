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