package com.mm.gemini.security;

import com.mm.gemini.base.model.CurrentUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiulong
 * @date 2019-01-22
 */

public class UserInfoDetails implements UserDetails {
    private static final long serialVersionUID = 6845713575400057817L;
    @Getter
    private Long id;
//    @Getter
//    private String openId;
    private String username;
    @Getter
    private String email;
    @Getter
    private Integer accountType;
    @Getter
    private Long relationId;
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserInfoDetails(Long id, String username, String email, Integer accountType, Long relationId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.accountType = accountType;
        this.relationId = relationId;
        this.authorities = authorities;
    }

    public static UserInfoDetails create(CurrentUser currentUser) {
        List<GrantedAuthority> authorities = currentUser.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toList());

        return new UserInfoDetails(currentUser.getUser().getId(),
                currentUser.getUser().getUsername(),
                currentUser.getUser().getEmail(),
                currentUser.getUser().getAccountType(),
                currentUser.getUser().getRelationUid(),
                authorities);
    }


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
