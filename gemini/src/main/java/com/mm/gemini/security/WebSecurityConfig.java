package com.mm.gemini.security;


import com.mm.gemini.security.filter.CustomCorsFilter;
import com.mm.gemini.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationEntryPoint unauthorizedHandler;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
    }
    /**
     * 设置 HTTP 验证规则
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 基于token，不需要csrf，关闭csrf验证
        httpSecurity.csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable() //不要UsernamePasswordAuthenticationFilter
                .httpBasic().disable(); //不要BasicAuthenticationFilter

            // 添加一个过滤器验证其他请求的Token是否合法
            httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, customUserDetailsService, unauthorizedHandler),
                    UsernamePasswordAuthenticationFilter.class);
            // 添加CORS过滤器
            httpSecurity.addFilterAfter(new CustomCorsFilter(), JwtAuthenticationFilter.class);
            httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
            // 禁用缓存
            httpSecurity.headers().cacheControl();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        // 放行swagger 和静态资源

        web.ignoring().antMatchers("/swagger-ui.html",
                "/v2/api-docs",
                "/configuration/**",
                "/swagger*/**",
                "/webjars/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js");
        //放行认证到controller
        web.ignoring().antMatchers(HttpMethod.POST,
                "/login",
                "/register",
                "/verifyEmailRandomCode",
                "/sendEmailRandomCode"
        );
    }
}