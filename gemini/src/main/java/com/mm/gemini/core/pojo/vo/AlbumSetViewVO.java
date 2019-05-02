package com.mm.gemini.core.pojo.vo;


import lombok.Data;

import java.util.List;

@Data
public class AlbumSetViewVO {
    private Long id;
    /**
     * 描述
     */
    private String description;
    /**
     * 地点
     */
    private String location;
    /**
     * 图片列表
     */
    private List<AlbumPicViewVO> pics;

    private Long uid;
}
