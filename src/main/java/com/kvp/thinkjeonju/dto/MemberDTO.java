package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    @NotBlank(message = "아이디를 작성해주세요.")
    @Length(min = 6, max = 15, message = "아이디는 6자 이상 15자 이하로 작성해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,15}$", message = "아이디는 영문, 숫자 조합으로 압력해주세요.")
    private String id;
    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Length(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하로 작성해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$", message = "비밀번호는 영문, 숫자, 특수문자 조합으로 압력해주세요.")
    private String password;
    @NotBlank(message = "이름을 작성해주세요.")
    @Length(min = 1, max = 20, message = "이름은 1자 이상 20자 이하로 작성해주세요.")
    private String name;

    public MemberDTO erasePassword() {
        this.password = "";
        return this;
    }
}