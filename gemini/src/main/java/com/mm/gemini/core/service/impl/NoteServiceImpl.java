package com.mm.gemini.core.service.impl;

import com.mm.gemini.core.model.Note;
import com.mm.gemini.core.mapper.NoteMapper;
import com.mm.gemini.core.service.NoteService;
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
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

}
