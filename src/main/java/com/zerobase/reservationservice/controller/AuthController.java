package com.zerobase.reservationservice.controller;

import com.zerobase.reservationservice.model.Auth;
import com.zerobase.reservationservice.security.TokenProvider;
import com.zerobase.reservationservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    /**
     * 회원 가입
     *
     * @param request (가입할 회원의 정보,권한)
     * @return 가입된 회원의 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        var result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인
     *
     * @param request (로그인할 회원의 정보)
     * @return 1시간동안 유효한 jwt 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }
}
