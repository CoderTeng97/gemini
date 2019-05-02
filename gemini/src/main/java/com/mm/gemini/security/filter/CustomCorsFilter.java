package com.mm.gemini.security.filter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @author ql
 */
public class CustomCorsFilter extends CorsFilter {

    public CustomCorsFilter() {
        super(configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");
        config.setMaxAge(36000L);
        config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
        //CORS
//        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
//        if (httpServletRequest.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
//            httpServletResponse.addHeader("Access-Control-Allow-Headers", "Authorization");
//            httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
//            httpServletResponse.addHeader("Access-Control-Max-Age", "1");
//            httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
    }
}