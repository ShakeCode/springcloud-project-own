package com.test.springcloud.model;

import lombok.Data;

import java.util.Date;

/**
 * The type Payload.
 * @param <T> the type parameter
 */
@Data
public class Payload<T> {
    private String id;
    private T userInfo;
    private Date expiration;
}