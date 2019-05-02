package com.mm.gemini.core.pojo.dto;

import lombok.Data;

@Data
public class VideoReleaseDTO {
    private String name;

    /**
     * 描述
     */
    private String description;

    private String url;
}
