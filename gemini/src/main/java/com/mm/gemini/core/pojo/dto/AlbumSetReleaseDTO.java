package com.mm.gemini.core.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 发布DTO
 */
@Data
public class AlbumSetReleaseDTO {


    /**
     * 描述
     */
    private String description;

    /**
     * 位置
     */
    private String location;

    /**
     * 图片url
     */
    List<String> urls;
}
