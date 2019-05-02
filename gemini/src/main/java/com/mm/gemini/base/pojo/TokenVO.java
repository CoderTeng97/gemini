package com.mm.gemini.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qiulong
 * @date 2019/1/17
 */
@ToString
@AllArgsConstructor
public class TokenVO extends ResponseBaseVO {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String token;
}
