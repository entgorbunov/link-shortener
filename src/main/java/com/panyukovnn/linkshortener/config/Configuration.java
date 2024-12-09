package com.panyukovnn.linkshortener.config;

import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import com.panyukovnn.linkshortener.service.impl.LinkInfoServiceImpl;
import com.panyukovnn.linkshortener.service.impl.LogExecutionTimeLinkInfoServiceProxy;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

	private final LinkInfoRepository linkInfoRepository;
	private final LinkInfoProperty linkInfoProperty;

	public Configuration(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty) {
		this.linkInfoRepository = linkInfoRepository;
		this.linkInfoProperty = linkInfoProperty;
	}

	@Bean
	public LinkInfoService linkInfoService() {
		LinkInfoServiceImpl linkInfoService = new LinkInfoServiceImpl(linkInfoProperty, linkInfoRepository);
		return new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
	}
}
