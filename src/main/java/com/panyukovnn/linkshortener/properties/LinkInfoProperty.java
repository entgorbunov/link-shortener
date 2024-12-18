package com.panyukovnn.linkshortener.properties;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "link-shortener")
public record LinkInfoProperty(
    @Min(value = 8, message = "Длина короткой ссылки не может быть менее 8")
    Integer shortLinkLength) {
}
