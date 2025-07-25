package com.learnova.classedge.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.MemberRole;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("path : {} ", path);
        if (request.getMethod().equals("OPTIONS") || path.startsWith("/api/v1/login")
                || path.startsWith("/api/v1/signup") || path.startsWith("/favicon.ico")
                || path.startsWith("/ws") || path.startsWith("/api/v1/refresh") || path.startsWith("/api/v1/admin")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        log.warn("token Header : {} ", header);

        // 가끔 다른 형태의 값이 들어올때 예외처리
        String accessToken = (header.contains(" ") ? header.split(" ")[1] : header);

        try {
            Map<String, Object> claims = JwtUtil.validationToken(accessToken);

            String email = (String) claims.get("email");
            String id = (String) claims.get("id");
            String name = (String) claims.get("memberName");
            String password = (String) claims.get("password");
            log.warn("password : {}", password);
            Boolean isWithdraw = ((Boolean) claims.get("isWithdraw"));
            log.warn("isWithdraw : {}", isWithdraw);

            MemberRole role = MemberRole.valueOf((String) claims.get("role"));
            String nickname = (String) claims.get("nickname");
            LoginType loginType = LoginType.valueOf((String) claims.get("loginType"));
            log.warn("check parse : {} ", loginType);
            MemberDto memberDto = new MemberDto(email, id, name, password, isWithdraw, role, nickname, loginType);
            log.warn("check dto : {} ", memberDto);

            // UsernamePasswordAuthenticationToken에 권한 추가
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDto,
                    memberDto.getPassword(), memberDto.getAuthorities());
            log.warn("auth : {} ", authentication.toString());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.warn("context ");

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof AccessDeniedException) {
                throw (AccessDeniedException) cause;
            }
            log.error("Error in JwtCheckFilter : {}", e);

            // JWT 오류 시 에러 메시지 전송
            Gson gson = new Gson();
            String jsonStr = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pWriter = response.getWriter();
            pWriter.println(jsonStr);
            pWriter.close();
        }

    }

}