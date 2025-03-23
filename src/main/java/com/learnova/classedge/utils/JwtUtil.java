package com.learnova.classedge.utils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.learnova.classedge.exception.CustomJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    // 임의의 값 설정 가능 (길이 주의!)
    private static String key = "adsflkasjfljweiajfoiasjfijlicxvjlixczihiore143214";
    
    public static String generateToken(Map<String, Object> claims, int min){
        SecretKey sKey = null;
        try{
            sKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
        String jwtStr = Jwts.builder()
                                .setHeader(Map.of("typ","JWT"))
                                .setClaims(claims)
                                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                                .signWith(sKey)
                                .compact();
        return jwtStr;
    }

    public static Map<String, Object> validationToken(String token){
        Map<String, Object> claims = null;

        try {
            SecretKey sKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));
            claims = Jwts.parserBuilder()
                                    .setSigningKey(sKey)
                                    .build()
                                    .parseClaimsJws(token)
                                    .getBody();

        } catch (MalformedJwtException e) {
            throw new CustomJwtException("Malformed Error, Check JWT Str" + e);
        } catch(ExpiredJwtException e){
            throw new CustomJwtException("Expired Token");
        }catch(InvalidClaimException e){
            throw new CustomJwtException("Invalid Claims");
        }catch(JwtException e){
            throw new CustomJwtException("JWT Excp");
        }catch(Exception e){
            throw new CustomJwtException("Error Validate Token : "+ e.getMessage());
        }
        return claims;
    }
}
