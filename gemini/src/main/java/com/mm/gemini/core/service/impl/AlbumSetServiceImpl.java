package com.mm.gemini.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.gemini.core.model.AlbumSet;
import com.mm.gemini.core.mapper.AlbumSetMapper;
import com.mm.gemini.core.pojo.vo.AlbumSetViewVO;
import com.mm.gemini.core.service.AlbumSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
@Service
public class AlbumSetServiceImpl extends ServiceImpl<AlbumSetMapper, AlbumSet> implements AlbumSetService {

    @Override
    public IPage<AlbumSetViewVO> getViewInfo(Integer pageNum, Integer pageSize, List<Long> uids) {
        return baseMapper.selectAlbumSetVOInfoByUIds(
                new Page<>(pageNum,pageSize),
                uids
                );
    }

    @Override
    public Long getOurPostPicTotalCount(List<Long> uids) {
        return baseMapper.selectPicTotalCountByUIds(uids);
    }
}
