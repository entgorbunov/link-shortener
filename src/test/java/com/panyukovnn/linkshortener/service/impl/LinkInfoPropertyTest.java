package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LinkInfoPropertyTest {

	@Autowired
	private LinkInfoProperty linkInfoProperty;

	@Autowired
	private LinkInfoService linkInfoService;

	@Test
	void shouldLoadCorrectShortLinkLength() {
		assertEquals(8, linkInfoProperty.getShortLinkLength());
	}


}
