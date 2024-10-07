package com.zerobase.reservationservice.security;

import com.zerobase.reservationservice.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000L * 60 * 60; // 1 hour

    private final MemberService memberService;

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급) 메서드
     *
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    /**
     * 토큰을 통해 보안을 체크하는 메서드
     *
     * @param jwt
     * @return 보안을 통해 확인된 인증
     */
    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 사용자 이름 꺼내는 메서드
     *
     * @param token
     * @return 사용자 이름
     */
    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * 토큰을 검증하는 메서드
     *
     * @param token
     * @return 토큰 검증 결과
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /**
     * 토큰에서 body 부분을 꺼내는 메서드
     *
     * @param token
     * @return 토큰의 body
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
