package com.mm.gemini.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mm.gemini.core.model.AlbumSet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.gemini.core.pojo.vo.AlbumSetViewVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
public interface AlbumSetService extends IService<AlbumSet> {
    IPage<AlbumSetViewVO> getViewInfo(Integer pageNum, Integer pageSize, List<Long> uids);

    Long getOurPostPicTotalCount(List<Long> uids);
}
