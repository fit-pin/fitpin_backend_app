package com.mzm.Fitpin.controller;

import com.mzm.Fitpin.dto.LoginDto;
import com.mzm.Fitpin.dto.LoginResponseDto;
import com.mzm.Fitpin.entity.Member;
import com.mzm.Fitpin.exception.CustomException;
import com.mzm.Fitpin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Member member = loginService.login(loginDto.getUserEmail(), loginDto.getUserPwd());
            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setUserEmail(member.getUserEmail());
            responseDto.setUserName(member.getUserName());
            responseDto.setUserNickname(member.getUserNickname());
            responseDto.setUserAddr(member.getUserAddr());
            responseDto.setUserGender(member.getUserGender());
            responseDto.setUserHeight(member.getUserHeight());
            responseDto.setUserWeight(member.getUserWeight());
            responseDto.setUserFit(member.getUserFit()); // Todo: 관련 코드 수정해야함!!!! - 완
            responseDto.setUserCash(member.getUserCash());

            return ResponseEntity.ok(responseDto);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "예상치 못한 오류가 발생했습니다."));
        }
    }
}
