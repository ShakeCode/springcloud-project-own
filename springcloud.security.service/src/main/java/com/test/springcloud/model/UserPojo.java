package com.test.springcloud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type User pojo.
 */
@Data
public class UserPojo implements UserDetails {
    private Integer id;

    private String username;

    private String password;

    private Integer status;

    private List<RolePojo> roles;

    /**
     * Gets authorities.
     * @return the authorities
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("ADMIN"));
        return auth;
    }

    /**
     * Gets password.
     * @return the password
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets username.
     * @return the username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Is account non expired boolean.
     * @return the boolean
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Is account non locked boolean.
     * @return the boolean
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Is credentials non expired boolean.
     * @return the boolean
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Is enabled boolean.
     * @return the boolean
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}