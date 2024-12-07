package com.panyukovnn.linkshortener;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.repository.impl.LinkInfoRepositoryImpl;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import com.panyukovnn.linkshortener.service.impl.LinkInfoServiceImpl;

import java.time.LocalDateTime;

public class LinkShortenerApp {
	public static void main(String[] args) {
		LinkInfoRepository repository = new LinkInfoRepositoryImpl();
		LinkInfoService linkService = new LinkInfoServiceImpl(repository);
		LinkInfoResponse linkInfo = linkService.createLinkInfo(new CreateShortLinkRequest(
			"https://github.com/entgorbunov/link-shortener",
			LocalDateTime.now().plusDays(7),
			"GitHub",
			true
		));
		System.out.println(linkInfo.getShortLink());
	}
}
