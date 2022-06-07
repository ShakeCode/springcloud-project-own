package com.springcloud.data.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Header info.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "HeaderInfo", description = "请求头接受实体")
public class HeaderInfo {
    @ApiModelProperty("租户编码")
    private String tenantCode;

    @ApiModelProperty("用户信息AES加密")
    private String userInfo;

    @ApiModelProperty("机器账号token")
    private String gcAuthentication;

    @ApiModelProperty(value = "语言", example = "zh-CN,en-US")
    private String lang;
}
