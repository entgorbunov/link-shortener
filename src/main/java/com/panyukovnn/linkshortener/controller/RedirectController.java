package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/short-link")
@RequiredArgsConstructor
public class RedirectController {

    private final LinkInfoService linkInfoService;

    @GetMapping("/{shortLink}")
    public ResponseEntity<String> redirect(@PathVariable String shortLink) {
        LinkInfoResponse linkInfo = linkInfoService.getByShortLink(shortLink);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
            .header(HttpHeaders.LOCATION, linkInfo.getLink())
            .build();
    }
}
