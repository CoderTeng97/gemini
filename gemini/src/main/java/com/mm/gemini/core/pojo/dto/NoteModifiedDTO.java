package com.mm.gemini.core.pojo.dto;

import lombok.Data;


@Data
public class NoteModifiedDTO {
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private Integer isOpen;
}
