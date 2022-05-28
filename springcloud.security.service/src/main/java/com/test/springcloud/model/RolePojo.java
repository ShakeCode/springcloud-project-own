package com.test.springcloud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * The type Role pojo.
 */
@Data
public class RolePojo implements GrantedAuthority {
    private Integer id;
    private String roleName;
    private String roleDesc;

    /**
     * Gets authority.
     * @return the authority
     */
    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
