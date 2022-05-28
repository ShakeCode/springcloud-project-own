package com.test.springcloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Account credentials.
 */
@ApiModel("用户信息实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentials {
    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户号码")
    private String password;
}
