package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.model.CommonRequest;
import com.panyukovnn.linkshortener.model.CommonResponse;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @PostMapping("/create")
    public CommonResponse<LinkInfoResponse> createShortLink(@RequestBody CommonRequest<CreateShortLinkRequest> request) {
        LinkInfoResponse linkInfo = linkInfoService.createLinkInfo(request.getData());

        return CommonResponse.<LinkInfoResponse>builder()
            .data(linkInfo)
            .uuid(UUID.randomUUID())
            .build();
    }

    @GetMapping("/{shortLink}")
    public CommonResponse<LinkInfoResponse> getByShortLink(@PathVariable String shortLink) {
        LinkInfoResponse linkInfo = linkInfoService.getByShortLink(shortLink);

        if (!linkInfo.getActive()) {
            return CommonResponse.<LinkInfoResponse>builder()
                .errorMessage("Ссылка неактивна")
                .uuid(UUID.randomUUID())
                .build();
        }

        if (linkInfo.getEndTime() != null && LocalDateTime.now().isAfter(linkInfo.getEndTime())) {
            return CommonResponse.<LinkInfoResponse>builder()
                .errorMessage("Ссылка недействительна")
                .uuid(UUID.randomUUID())
                .build();
        }

        return CommonResponse.<LinkInfoResponse>builder()
            .data(linkInfo)
            .uuid(UUID.randomUUID())
            .build();
    }

    @GetMapping
    public CommonResponse<List<LinkInfoResponse>> getAllLinks() {
        List<LinkInfoResponse> links = linkInfoService.findByFilter();

        return CommonResponse.<List<LinkInfoResponse>>builder()
            .data(links)
            .uuid(UUID.randomUUID())
            .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteLink(@PathVariable UUID id) {
        linkInfoService.deleteById(id);

        return CommonResponse.<Void>builder()
            .uuid(UUID.randomUUID())
            .build();
    }
}
