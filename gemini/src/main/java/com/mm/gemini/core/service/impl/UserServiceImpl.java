package com.mm.gemini.core.service.impl;

import com.mm.gemini.core.model.User;
import com.mm.gemini.core.mapper.UserMapper;
import com.mm.gemini.core.pojo.vo.UserBaseInfoVO;
import com.mm.gemini.core.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String getHeadPortrait(Long uid) {
        return baseMapper.selectHeadPortraitByUid(uid);
    }

    @Override
    public void setRelationUid(Long uid,Long relationUid) {
         baseMapper.updateRelationUidById(uid,relationUid);
    }

    @Override
    public Integer getOnlineDays(Long uid) {
        LocalDateTime dateTime = baseMapper.selectGmtCreateByUid(uid);
        if (dateTime == null){
            return  null;
        }
        return  Period.between(dateTime.now().toLocalDate(), LocalDate.now()).getDays();
    }

    @Override
    public UserBaseInfoVO getUserBaseInfo(Long id) {
        return baseMapper.selectBaseInfoById(id);
    }
}
