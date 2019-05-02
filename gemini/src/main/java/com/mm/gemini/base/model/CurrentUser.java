package com.mm.gemini.base.model;

import com.mm.gemini.core.model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author qiulong
 * @date 2019-01-22
 */
public class CurrentUser implements Serializable {
    private static final long serialVersionUID = 9118999548808496433L;
    @Getter@Setter
    private User user;
    @Setter@Getter
    private List<Role> roles;

    public CurrentUser(User user) {
        this.user = user;
    }
}
