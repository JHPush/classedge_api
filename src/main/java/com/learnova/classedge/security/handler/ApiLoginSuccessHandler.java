package com.learnova.classedge.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.google.gson.Gson;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.utils.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        MemberDto memberDto = (MemberDto)authentication.getPrincipal();
        Map<String, Object> claims = memberDto.getClaims();
        String accessToken = JwtUtil.generateToken(claims, 60);
        String refreshToken = JwtUtil.generateToken(claims, 60*24);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter pw = response.getWriter();
        pw.println(jsonStr);
        pw.close();
    }

}
