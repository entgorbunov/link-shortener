package com.panyukovnn.linkshortener.config;

import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import com.panyukovnn.linkshortener.service.impl.LinkInfoServiceImpl;
import com.panyukovnn.linkshortener.service.impl.LogExecutionTimeLinkInfoServiceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkShortenerConfig {

	@Bean
	public LinkInfoService linkInfoService(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty) {
		LinkInfoServiceImpl linkInfoService = new LinkInfoServiceImpl(linkInfoProperty, linkInfoRepository);
		return new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
	}

}
