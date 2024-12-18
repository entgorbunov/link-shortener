package com.panyukovnn.linkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panyukovnn.linkshortener.dto.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private T data;
    private String errorMessage;
    private UUID id;
    private List<ValidationError> validationErrors;
}
