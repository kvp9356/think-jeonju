package com.kvp.thinkjeonju.support;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.exception.common.UnAuthenticatedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

public class SessionUtil {
    public static final String LOGIN_USER = "loginUser";

    public static MemberDTO getLoginUserFromWebRequest(NativeWebRequest webRequest) {
        MemberDTO memberDTO = (MemberDTO) webRequest.getAttribute(LOGIN_USER, WebRequest.SCOPE_SESSION);
        if(memberDTO == null)
            throw new UnAuthenticatedException("로그인이 필요합니다.");
        return memberDTO;
    }
}
