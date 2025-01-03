package com.panyukovnn.linkshortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterLinkInfoRequest {

    private String linkPart;
    private LocalDateTime endTimeFrom;
    private LocalDateTime endTimeTo;
    private String descriptionPart;
    private Boolean active;
}
