document.addEventListener("DOMContentLoaded", () => {
    $("#login-modal").modal("show");

    $("#login").on("click", () => {
        showModal("login");
    });
    $("#join").on("click", () => {
        showModal("join");
    });

    $("#login-modal").on("hidden.bs.modal", () => {
        clearModal("login");
        if(!$_("#login-modal").classList.contains("show") && !$_("#join-modal").classList.contains("show"))
            reloadHome();
    });
    $("#join-modal").on("hidden.bs.modal", () => {
        clearModal("join");
        if(!$_("#login-modal").classList.contains("show") && !$_("#join-modal").classList.contains("show"))
            reloadHome();
    });

    $("#login-form-btn").on("click", () => {
        showModal("login");
        hideModal("join");
    });
    $("#join-form-btn").on("click", () => {
        showModal("join");
        hideModal("login");
    });

});