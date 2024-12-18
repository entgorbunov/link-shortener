package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import com.panyukovnn.linkshortener.dto.common.CommonResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Test
    void shouldCreateShortLinkSuccessfully() {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://example.com")
            .active(true)
            .description("Test link")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setData(createRequest);

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

        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(expectedResponse);
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

        ResponseEntity<String> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse.getLink());
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

    @Test
    void shouldGetAllLinksSuccessfully() {
        List<LinkInfoResponse> expectedLinks = List.of(
            LinkInfoResponse.builder()
                .id(UUID.randomUUID())
                .link("http://example1.com")
                .shortLink("abc123")
                .active(true)
                .description("First link")
                .endTime(LocalDateTime.now().plusDays(1))
                .openingCount(5L)
                .build(),
            LinkInfoResponse.builder()
                .id(UUID.randomUUID())
                .link("http://example2.com")
                .shortLink("def456")
                .active(true)
                .description("Second link")
                .endTime(LocalDateTime.now().plusDays(2))
                .openingCount(3L)
                .build()
        );

        when(linkInfoService.findByFilter())
            .thenReturn(expectedLinks);

        CommonResponse<List<LinkInfoResponse>> response = linkInfoController.getAllLinks();

        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(expectedLinks);
        assertThat(response.getData()).hasSize(2);
        verify(linkInfoService, times(1)).findByFilter();
    }

    @Test
    void shouldDeleteLinkSuccessfully() {
        UUID id = UUID.randomUUID();
        doNothing().when(linkInfoService).deleteById(id);

        CommonResponse<Void> response = linkInfoController.deleteLink(id);

        assertThat(response).isNotNull();
        verify(linkInfoService, times(1)).deleteById(id);
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

        ResponseEntity<String> response = linkInfoController.getByShortLink(shortLink);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(linkWithNoEndTime.getLink());
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

}
