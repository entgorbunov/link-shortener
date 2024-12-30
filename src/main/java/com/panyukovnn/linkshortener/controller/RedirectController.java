package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/r")
@RequiredArgsConstructor
public class RedirectController {

    private final LinkInfoService linkInfoService;

    @GetMapping("/{shortLink}")
    public ResponseEntity<String> redirect(@PathVariable String shortLink) {
        LinkInfoResponse linkInfo = linkInfoService.getByShortLink(shortLink);
        return ResponseEntity.ok(linkInfo.getLink());
    }
}
