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
public class JwtCheckFilter extends OncePerRequestFilter{

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // preflight 요청 - OPTIONS (리퀘스트 사전검증), /api/v1 경로로 오는 요청은 필터를 거치지 않음
        if(request.getMethod().equals("OPTIONS") || path.startsWith("/api/v1")){
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 가끔 다른 형태의 값이 들어올때 예외처리
        String accessToken = (header.contains(" ")? header.split(" ")[1]: header); 
        
        Map<String, Object> claims = JwtUtil.validationToken(accessToken);
        
        try {
            String email = (String)claims.get("m_email");
            String id = (String)claims.get("m_id");
            String name = (String)claims.get("m_name");
            String password = (String)claims.get("m_password");
            Boolean isWithdraw = (0 != Integer.parseInt((String)claims.get("m_is_withdraw"))); // Boolean.parseBoolean() 이용하면 코드 간소화 가능
            MemberRole role = MemberRole.valueOf((String)claims.get("m_role"));
            String nickname = (String)claims.get("m_nickname");
            LoginType loginType = LoginType.valueOf((String)claims.get("m_login_type"));
    
            MemberDto memberDto = new MemberDto(email, id, name
                                                , password, isWithdraw, role
                                                , nickname, loginType);
    
            // UsernamePasswordAuthenticationToken에 권한 추가
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberDto, memberDto.getPassword(), memberDto.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if(cause instanceof AccessDeniedException){
                throw (AccessDeniedException)cause;
            }
            log.error("Error in JwtCheckFilter : {}", e);
            
            // JWT 오류 시 에러 메시지 전송
            Gson gson = new Gson();
            String jsonStr = gson.toJson(Map.of("error","ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pWriter = response.getWriter();
            pWriter.println(jsonStr);
            pWriter.close();
        }

    }
    
}