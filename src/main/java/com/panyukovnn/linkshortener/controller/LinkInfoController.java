package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import com.panyukovnn.linkshortener.dto.common.CommonResponse;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @PostMapping("/create")
    public CommonResponse<LinkInfoResponse> createShortLink(@RequestBody @Valid CommonRequest<CreateShortLinkRequest> request) {
        LinkInfoResponse linkInfo = linkInfoService.createLinkInfo(request.getBody());
        return CommonResponse.<LinkInfoResponse>builder()
            .body(linkInfo)
            .id(UUID.randomUUID())
            .build();
    }

    @GetMapping
    public CommonResponse<List<LinkInfoResponse>> getAllLinks() {
        List<LinkInfoResponse> links = linkInfoService.findByFilter();
        return CommonResponse.<List<LinkInfoResponse>>builder()
            .body(links)
            .id(UUID.randomUUID())
            .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteLink(@PathVariable UUID id) {
        linkInfoService.deleteById(id);
        return CommonResponse.<Void>builder()
            .id(UUID.randomUUID())
            .build();
    }
}
