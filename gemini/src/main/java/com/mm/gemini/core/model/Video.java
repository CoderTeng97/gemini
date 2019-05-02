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
@TableName("tb_video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * 描述
     */
    private String description;

    private String url;

    /**
     * 发布时间
     */
    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Integer isDel;

    /**
     * 关联用户ID
     */
    private Long uid;


}
