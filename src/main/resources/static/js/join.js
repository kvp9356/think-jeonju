class Join {
    constructor() {
        $("#join-btn").on("click", this.join.bind(this));
    }
    validateName() {
        if(this.name.length <= 0) {
            $_("#join-name-error span").innerText = "이름을 입력해주세요.";
            $_("#join-name-error").style.display = "";
            return false;
        }
        if(this.name.length < 1 || this.name.length > 20) {
            $_("#join-name-error span").innerText = "이름은 1자 이상 20자 이하로 입력해주세요.";
            $_("#join-name-error").style.display = "";
            return false;
        }
        $_("#join-name-error").style.display = "none";
        return true;
    }
    validatePassword() {
        if(this.password.length <= 0) {
            $_("#join-password-error span").innerText = "비밀번호를 입력해주세요.";
            $_("#join-password-error").style.display = "";
            return false;
        }
        if(this.password.length < 8 || this.password.length > 15) {
            $_("#join-password-error span").innerText = "비밀번호는 8자 이상 15자 이하로 입력해주세요.";
            $_("#join-password-error").style.display = "";
            return false;
        }
        const passwordRegex = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$");
        if(!passwordRegex.test(this.password)) {
            $_("#join-password-error span").innerText = "비밀번호는 영문, 숫자, 특수문자 조합으로 입력해주세요.";
            $_("#join-password-error").style.display = "";
            return false;
        }
        $_("#join-password-error").style.display = "none";
        return true;
    }
    validateId() {
        if(this.id.length <= 0) {
            $_("#join-id-error span").innerText = "아이디를 입력해주세요.";
            $_("#join-id-error").style.display = "";
            return false;
        }
        if(this.id.length < 6 || this.id.length > 15) {
            $_("#join-id-error span").innerText = "아이디는 6자 이상 15자 이하로 입력해주세요.";
            $_("#join-id-error").style.display = "";
            return false;
        }
        const idRegex = new RegExp("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,15}$");
        if(!idRegex.test(this.id)) {
            $_("#join-id-error span").innerText = "아이디는 영문, 숫자 조합으로 입력해주세요.";
            $_("#join-id-error").style.display = "";
            return false;
        }
        $_("#join-id-error").style.display = "none";
        return true;
    }
    join() {
        this.id = $_("#join-id").value;
        this.password = $_("#join-password").value;
        this.name = $_("#join-name").value;

        if(!this.validateId() | !this.validatePassword() | !this.validateName()) {
            return;
        }

        const joinForm = {
            "id" : this.id,
            "password" : this.password,
            "name" : this.name
        };
        $.ajax({
            url: "/api/members",
            method: "POST",
            data: JSON.stringify(joinForm),
            contentType: "application/json",
            success: () => {
                $_("#join-error").style.display = "none";
                if(location.href.includes('/loginError')) {
                    reloadHome();
                } else {
                    location.reload();
                }
            },
            error: () => {
                $_("#join-error span").innerText = "아이디, 비밀번호, 이름을 다시 확인해주세요.";
                $_("#join-error").style.display = "";
            }
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Join();
});