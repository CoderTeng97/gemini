package com.mm.gemini.core.mapper;

import com.mm.gemini.core.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.gemini.core.pojo.vo.UserBaseInfoVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MUMA
 * @since 2019-03-19
 */
public interface UserMapper extends BaseMapper<User> {
    String selectHeadPortraitByUid(Long uid);

    LocalDateTime selectGmtCreateByUid(Long uid);

    int updateRelationUidById(
            @Param("uid")
            Long uid,
            @Param("relationUid")
            Long relationUid);

    UserBaseInfoVO selectBaseInfoById(
            Long id
    );
}
