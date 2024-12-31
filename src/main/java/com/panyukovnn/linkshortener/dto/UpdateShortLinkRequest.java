package com.panyukovnn.linkshortener.dto;

import com.panyukovnn.linkshortener.validation.ValidUUID;
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
public class UpdateShortLinkRequest {

    @ValidUUID
    private String id;
    @Pattern(regexp = "^http[s]?://.+\\..+", message = "В ссылке допущена ошибка")
    private String link;
    @Future(message = "Дата окончания ссылки должна быть в будущем")
    private LocalDateTime endTime;
    @NotEmpty(message = "Описание не должно быть пустым")
    private String description;
    @NotNull(message = "Признак активности не может быть null")
    private Boolean active;
}
