package com.panyukovnn.linkshortener.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse<T> {
    private T data;
    private boolean success;
    private String error;
}
