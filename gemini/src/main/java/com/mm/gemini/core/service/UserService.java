package com.mm.gemini.core.service;

import com.mm.gemini.core.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.gemini.core.pojo.vo.UserBaseInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
public interface UserService extends IService<User> {

    String getHeadPortrait(Long uid);

    void setRelationUid(Long uid,Long relationUid);

    Integer getOnlineDays(Long uid);

    UserBaseInfoVO getUserBaseInfo(Long id);
}
