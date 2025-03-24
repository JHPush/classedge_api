package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.exception.CustomJwtException;
import com.learnova.classedge.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ApiRefreshController {

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> getRefresh(@RequestHeader("Authorization") String authHeader,
            @RequestParam("refreshToken") String refreshToken) {

        if (refreshToken == null)
            throw new CustomJwtException("NULL_REFRESH_TOKEN");
        if (authHeader == null || authHeader.length() < 7)
            throw new CustomJwtException("INVALID_AUTH_HEADER");

        String accessToken = authHeader.contains(" ") ? authHeader.split(" ")[1] : authHeader;

        if (!checkExpiredToken(accessToken)) {
            return new ResponseEntity<>(Map.of("accessToken", accessToken, "refreshToken", refreshToken),
                    HttpStatus.OK);
        }

        Map<String, Object> claims = JwtUtil.validationToken(refreshToken);
        String newAccessToken = JwtUtil.generateToken(claims, 30);

        String newRefreshToken = checkRefreshTime((Integer) claims.get("exp")) ? JwtUtil.generateToken(claims, 60)
                : refreshToken;
        log.info("Generated New Token : {} ", newAccessToken);

        return new ResponseEntity<>(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken),
                HttpStatus.OK);
    }

    public boolean checkExpiredToken(String accessToken) {
        try {
            JwtUtil.validationToken(accessToken);
        } catch (Exception e) {
            log.info("EXX : {} ", e.getMessage());
            return (e.getMessage().equals("Expired Token")) ? true : false;
        }
        return false;
    }

    public boolean checkRefreshTime(Integer expire) {
        Date exeDate = new Date((long) expire * 60 * 1000);
        long gap = exeDate.getTime();
        long leftMin = gap / (60 * 1000);
        return leftMin < 60;
    }

}
