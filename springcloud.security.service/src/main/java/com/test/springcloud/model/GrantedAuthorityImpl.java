package com.test.springcloud.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * The type Granted authority.
 * 一个权限类型，负责存储权限和角色
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private String authority;

    /**
     * Instantiates a new Granted authority.
     * @param authority the authority
     */
    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    /**
     * Sets authority.
     * @param authority the authority
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
