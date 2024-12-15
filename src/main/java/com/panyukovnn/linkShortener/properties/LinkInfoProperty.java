package com.panyukovnn.linkshortener.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "link-shortener")
public record LinkInfoProperty(Integer shortLinkLength) {
}
