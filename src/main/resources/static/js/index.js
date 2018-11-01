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

    $("#login").removeAttr("href");
    $("#join").removeAttr("href");
    $("#logout").removeAttr("href");
});