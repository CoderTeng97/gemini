package com.mm.gemini.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_album_set")
public class AlbumSet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片集名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 位置
     */
    private String location;

    private Long uid;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Integer isDel;


}
