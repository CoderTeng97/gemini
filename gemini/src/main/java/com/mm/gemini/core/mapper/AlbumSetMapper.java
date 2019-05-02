package com.mm.gemini.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.gemini.core.model.AlbumSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.gemini.core.pojo.vo.AlbumSetViewVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
public interface AlbumSetMapper extends BaseMapper<AlbumSet> {
    /**
     * 查询相册浏览信息
     * @param page
     * @param ids
     * @return
     */
    IPage<AlbumSetViewVO> selectAlbumSetVOInfoByUIds(
            Page<AlbumSetViewVO > page,
            @Param("ids")
            List<Long> ids
    );

    /**
     * 查询图片总数通过多个uid
     * @param ids
     * @return
     */
    Long selectPicTotalCountByUIds(
            @Param("ids")
            List<Long> ids
    );
}
