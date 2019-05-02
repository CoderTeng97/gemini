package com.mm.gemini.security;


import com.mm.gemini.base.constant.JWTConstant;
import com.mm.gemini.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author ql
 */
@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = 7455531721470217295L;

    public String generateToken(Map<String, Object> claims) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + JWTConstant.EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                .compact();
    }

    private boolean canRefreshToken(Claims claims) {
        Date expirationDate = claims.getExpiration();
        Date expectDate = new Date(expirationDate.getTime() + JWTConstant.ONE_DAY);
        // 过期一天不到,refresh
        return expirationDate.before(expectDate);
    }

    public String refreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWTConstant.SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            Claims claims = getAllClaimsFromToken(token);
            if (canRefreshToken(claims)) {
                return generateToken(claims);
            }
        } catch (Exception ignore){
            return null;
        }
        return null;
    }


    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(JWTConstant.SECRET).parseClaimsJws(token).getBody();
        } catch (SignatureException ex) {
            throw new JwtAuthenticationException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtAuthenticationException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtAuthenticationException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtAuthenticationException("JWT claims string is empty.");
        }
    }

}