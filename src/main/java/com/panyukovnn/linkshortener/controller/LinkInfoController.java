package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import com.panyukovnn.linkshortener.dto.common.CommonResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/link-infos")
@RequiredArgsConstructor
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @PostMapping("/create")
    public CommonResponse<LinkInfoResponse> createShortLink(@RequestBody @Valid CommonRequest<CreateShortLinkRequest> request) {
        log.info("Поступил запрос на создание короткой ссылки: {}", request);

        LinkInfoResponse linkInfo = linkInfoService.createLinkInfo(request.getBody());

        log.info("Короткая ссылка создана успешно: {}", linkInfo);

        return CommonResponse.<LinkInfoResponse>builder()
            .body(linkInfo)
            .id(UUID.randomUUID())
            .build();
    }

    @PostMapping("/filter")
    public CommonResponse<List<LinkInfoResponse>> getLinkInfos(@RequestBody @Valid CommonRequest<FilterLinkInfoRequest> request) {
        List<LinkInfoResponse> linkInfoResponses = linkInfoService.findByFilter(request.getBody());

        return CommonResponse.<List<LinkInfoResponse>>builder()
            .id(UUID.randomUUID())
            .body(linkInfoResponses)
            .build();
    }

    @PutMapping()
    public CommonResponse<LinkInfoResponse> updateShortLink(
        @RequestBody @Valid CommonRequest<UpdateShortLinkRequest> request
    ) {
        log.info("Поступил запрос на обновление короткой ссылки: {}", request);

        LinkInfoResponse updatedLinkInfo = linkInfoService.updateLinkInfo(request.getBody());

        log.info("Короткая ссылка обновлена успешно: {}", updatedLinkInfo);

        return CommonResponse.<LinkInfoResponse>builder()
            .body(updatedLinkInfo)
            .id(UUID.randomUUID())
            .build();
    }

    @GetMapping
    public CommonResponse<List<LinkInfoResponse>> getAllLinks() {
        log.info("Поступил запрос на получение всех ссылок");

        List<LinkInfoResponse> links = linkInfoService.findAll();

        log.info("Успешно получен список ссылок, количество: {}", links.size());

        return CommonResponse.<List<LinkInfoResponse>>builder()
            .body(links)
            .id(UUID.randomUUID())
            .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteLink(@PathVariable UUID id) {
        log.info("Поступил запрос на удаление ссылки с id: {}", id);

        linkInfoService.deleteById(id);

        log.info("Ссылка с id {} успешно удалена", id);

        return CommonResponse.<Void>builder()
            .id(UUID.randomUUID())
            .build();
    }
}
