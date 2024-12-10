package com.panyukovnn.linkshortener.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@Setter
@ConfigurationProperties(prefix = "link-shortener")
public class LinkInfoProperty {

	private final Integer shortLinkLength;

	@ConstructorBinding
	public LinkInfoProperty(Integer shortLinkLength) {
		this.shortLinkLength = shortLinkLength;
	}
}
