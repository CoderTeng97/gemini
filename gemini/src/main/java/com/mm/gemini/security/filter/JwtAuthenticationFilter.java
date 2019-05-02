package com.mm.gemini.security.filter;


import com.mm.gemini.base.constant.JWTConstant;
import com.mm.gemini.base.pojo.ResponseErrorVO;
import com.mm.gemini.security.CustomUserDetailsService;
import com.mm.gemini.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ql
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    private CustomUserDetailsService customUserDetailsService;

    private AuthenticationEntryPoint authenticationEntryPoint;


    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService, AuthenticationEntryPoint unauthorizedHandler) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = unauthorizedHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = httpServletRequest.getHeader(JWTConstant.HEADER_STRING);

        //测试环境放行
        String env = System.getProperty("user.env");
        if ("local".equals(env)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        //已经完成认证、token为空、token前缀错误
        if (authHeader == null || !authHeader.startsWith(JWTConstant.TOKEN_PREFIX)) {
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),"禁止访问");
            return;
        }
        String token = authHeader.substring(7);

        try {
            // 2019-02-21 将validToken方法放到到getAllClaimsFromToken
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUser(claims);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}