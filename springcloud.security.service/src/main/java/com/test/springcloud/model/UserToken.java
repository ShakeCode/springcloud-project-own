package com.test.springcloud.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * The type User token.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserToken {
    /**
     * 用户ID
     */
    private String userId;


    /**
     * 用户登录账户
     */
    private String userNo;


    /**
     * 用户中文名
     */
    private String userName;
}