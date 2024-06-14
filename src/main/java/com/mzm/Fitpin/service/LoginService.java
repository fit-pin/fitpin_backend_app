package com.mzm.Fitpin.service;

import com.mzm.Fitpin.entity.Member;
import com.mzm.Fitpin.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    public Member login(String userEmail, String password) {
        Member member = loginMapper.findByUserEmail(userEmail);
        //일단 이부분은 디버깅하기 편하게 로그인 과정중 틀린 부분이 어디인지 명시했습니다. 여유 나면 고칠것
        if (member == null) {
            throw new RuntimeException("이메일을 찾을 수 없습니다.");
        }
        if (!member.getUserPwd().equals(password)) {
            throw new RuntimeException("비밀번호가 틀립니다.");
        }else {
            return member;
        }


    }
}
