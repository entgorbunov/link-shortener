package com.panyukovnn.linkshortener.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CommonResponse<T> {
    private T data;
    private String errorMessage;
    private UUID id;
}
