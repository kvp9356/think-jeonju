class Login {
    constructor() {
        $("#login-btn").on("click", this.login.bind(this));
    }
    validateId() {
        if(this.id.length <= 0) {
            $_("#login-id-error span").innerText = "아이디를 입력해주세요.";
            $_("#login-id-error").style.display = "";
            return false;
        }
        $_("#login-id-error").style.display = "none";
        return true;
    }
    validatePassword() {
        if(this.password.length <= 0) {
            $_("#login-password-error span").innerText = "비밀번호를 입력해주세요.";
            $_("#login-password-error").style.display = "";
            return false;
        }
        $_("#login-password-error").style.display = "none";
        return true;
    }
    login() {
        this.id = $_("#login-id").value;
        this.password = $_("#login-password").value;

        if(!this.validateId() | !this.validatePassword()) {
            return;
        }

        const loginForm = {
            "id" : this.id,
            "password" : this.password
        };
        $.ajax({
            url: "/api/members/login",
            method: "POST",
            data: JSON.stringify(loginForm),
            contentType: "application/json",
            success: () => {
                $_("#login-error").style.display = "none";
                reloadHome();
            },
            error: () => {
                $_("#login-error span").innerText = "아이디, 비밀번호를 다시 확인해주세요.";
                $_("#login-error").style.display = "";
            }
        })
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Login();
});