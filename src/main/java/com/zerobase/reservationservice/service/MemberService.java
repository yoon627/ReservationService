package com.zerobase.reservationservice.service;

import com.zerobase.reservationservice.model.Auth;
import com.zerobase.reservationservice.persist.MemberRepository;
import com.zerobase.reservationservice.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * username으로 회원 정보를 불러오는 메서드
     *
     * @param username
     * @return 회원 정보
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    /**
     * 회원 등록하는 메서드
     *
     * @param member (회원에 대한 정보)
     * @return 저장된 회원 정보
     */
    public MemberEntity register(Auth.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + member.getUsername());
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    /**
     * 로그인하는 매서드
     *
     * @param member (로그인 정보)
     * @return 로그인된 회원 정보
     */
    public MemberEntity authenticate(Auth.SignIn member) {
        var user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
