package com.mm.gemini.security;

import com.mm.gemini.base.model.CurrentUser;
import com.mm.gemini.base.model.Role;
import com.mm.gemini.core.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author ql
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    public UserDetails loadUser(Claims claims) {
        User user = new User();
        System.out.println(claims.getSubject());
        user.setId(Long.parseLong(claims.get("uid").toString()));
        user.setAccountType((Integer.valueOf(claims.get("accountType").toString())));
        user.setRelationUid(Long.parseLong(claims.get("relationUid").toString()));
        CurrentUser currentUser = new CurrentUser(user);
        ArrayList<Role> roles = new ArrayList<>();
        //TODO 所有人都是User，backend项目中操作人员可以设置为Admin
        roles.add(Role.ROLE_USER);
        currentUser.setRoles(roles);
        return UserInfoDetails.create(currentUser);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}