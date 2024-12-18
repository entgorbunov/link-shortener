package com.panyukovnn.linkshortener.dto.common;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CommonRequest<T> {
    @Valid
    private T data;
}
