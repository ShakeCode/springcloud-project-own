package com.springcloud.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Camel case user.
 */
// 序列化为驼峰  或引入HttpMessageConverters
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CamelCaseUser {
    private String userName;

    private String userId;

    private String userCode;

    private List<String> addressList = new ArrayList<>();
}
