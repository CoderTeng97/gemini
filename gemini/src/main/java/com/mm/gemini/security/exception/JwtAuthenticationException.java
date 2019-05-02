package com.mm.gemini.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author qiulong
 * @date 2019-01-22
 */
public class JwtAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = -2602753637709621972L;


    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
