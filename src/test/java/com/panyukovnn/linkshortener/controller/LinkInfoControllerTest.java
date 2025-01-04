package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import com.panyukovnn.linkshortener.dto.common.CommonResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
    "link-shortener.short-link-length=8",
    "logging.enable-log-execution-time=true"
})
class LinkInfoControllerTest {

    @MockBean
    private LinkInfoService linkInfoService;

    @Autowired
    private LinkInfoController linkInfoController;

    @Autowired
    private RedirectController redirectController;

    @Test
    void shouldCreateShortLinkSuccessfully() {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://example.com")
            .active(true)
            .description("Test link")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        LinkInfoResponse expectedResponse = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link(createRequest.getLink())
            .shortLink("testShort")
            .active(createRequest.getActive())
            .description(createRequest.getDescription())
            .endTime(createRequest.getEndTime())
            .openingCount(0L)
            .build();

        when(linkInfoService.createLinkInfo(any(CreateShortLinkRequest.class)))
            .thenReturn(expectedResponse);

        CommonResponse<LinkInfoResponse> response = linkInfoController.createShortLink(request);

        assertNotNull(response, "Ответ не должен быть null");

        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getBody()).isEqualTo(expectedResponse)
        );

        verify(linkInfoService, times(1)).createLinkInfo(createRequest);
    }

    @Test
    void shouldGetLinkByShortLinkSuccessfully() {
        String shortLink = "abc123";
        LinkInfoResponse expectedResponse = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(shortLink)
            .active(true)
            .description("Test link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(10L)
            .build();

        when(linkInfoService.getByShortLink(shortLink))
            .thenReturn(expectedResponse);

        ResponseEntity<String> response = redirectController.redirect(shortLink);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT),
            () -> assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION))
                .isEqualTo(expectedResponse.getLink())
        );

        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

    @Test
    void shouldGetAllLinksSuccessfully() {
        List<LinkInfoResponse> expectedLinks = List.of(
            LinkInfoResponse.builder()
                .id(UUID.randomUUID())
                .link("http://example1.com")
                .endTime(LocalDateTime.now().plusDays(1))
                .description("First link")
                .active(true)
                .shortLink("abc123")
                .openingCount(5L)
                .build(),
            LinkInfoResponse.builder()
                .id(UUID.randomUUID())
                .link("http://example2.com")
                .endTime(LocalDateTime.now().plusDays(2))
                .description("Second link")
                .active(true)
                .shortLink("def456")
                .openingCount(3L)
                .build()
        );

        PageImpl<LinkInfoResponse> expectedPage = new PageImpl<>(expectedLinks);

        FilterLinkInfoRequest filterLinkInfoRequest = new FilterLinkInfoRequest();

        when(linkInfoService.findByFilter(filterLinkInfoRequest))
            .thenReturn(expectedPage);

        CommonRequest<FilterLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(filterLinkInfoRequest);

        CommonResponse<Page<LinkInfoResponse>> response = linkInfoController.getLinkInfos(request);

        assertNotNull(response, "Ответ не должен быть null");
        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getBody()).isNotNull(),
            () -> assertThat(response.getBody().getContent()).isEqualTo(expectedLinks),
            () -> assertThat(response.getBody().getContent()).hasSize(2)
        );
        verify(linkInfoService, times(1)).findByFilter(filterLinkInfoRequest);
    }

    @Test
    void shouldDeleteLinkSuccessfully() {
        UUID id = UUID.randomUUID();
        doNothing().when(linkInfoService).deleteById(id);

        CommonResponse<Void> response = linkInfoController.deleteLink(id);

        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> verify(linkInfoService, times(1)).deleteById(id)
        );
    }

    @Test
    void shouldSucceedWhenLinkHasNoEndTime() {
        String shortLink = "abc123";
        LinkInfoResponse linkWithNoEndTime = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(shortLink)
            .active(true)
            .description("Test link")
            .endTime(null)
            .openingCount(10L)
            .build();

        when(linkInfoService.getByShortLink(shortLink))
            .thenReturn(linkWithNoEndTime);

        ResponseEntity<String> response = redirectController.redirect(shortLink);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT),
            () -> assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo(linkWithNoEndTime.getLink())
        );
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

    @Test
    void shouldUpdateShortLinkSuccessfully() {

        UpdateShortLinkRequest updateRequest = UpdateShortLinkRequest.builder()
            .id(UUID.randomUUID().toString())
            .link("http://updated.com")
            .active(true)
            .description("Updated test link")
            .endTime(LocalDateTime.now().plusDays(2))
            .build();

        CommonRequest<UpdateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(updateRequest);

        LinkInfoResponse expectedResponse = LinkInfoResponse.builder()
            .id(UUID.fromString(updateRequest.getId()))
            .link(updateRequest.getLink())
            .shortLink("updatedShort")
            .active(updateRequest.getActive())
            .description(updateRequest.getDescription())
            .endTime(updateRequest.getEndTime())
            .openingCount(5L)
            .build();

        doReturn(expectedResponse).when(linkInfoService).updateLinkInfo(any(UpdateShortLinkRequest.class));

        CommonResponse<LinkInfoResponse> response = linkInfoController.updateShortLink(request);

        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getBody()).isNotNull(),
            () -> assertThat(response.getBody()).isEqualTo(expectedResponse),
            () -> assertThat(response.getBody().getLink()).isEqualTo(updateRequest.getLink()),
            () -> assertThat(response.getBody().getDescription()).isEqualTo(updateRequest.getDescription()),
            () -> assertThat(response.getBody().getActive()).isEqualTo(updateRequest.getActive()),
            () -> assertThat(response.getBody().getEndTime()).isEqualTo(updateRequest.getEndTime())
        );

        verify(linkInfoService, times(1)).updateLinkInfo(updateRequest);
    }

}
