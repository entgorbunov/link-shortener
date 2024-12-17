package com.panyukovnn.linkshortener.model;

import lombok.Data;

@Data
public class CommonRequest<T> {
    private T data;
}
