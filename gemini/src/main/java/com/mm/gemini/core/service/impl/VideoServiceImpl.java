package com.mm.gemini.core.service.impl;

import com.mm.gemini.core.model.Video;
import com.mm.gemini.core.mapper.VideoMapper;
import com.mm.gemini.core.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

}
