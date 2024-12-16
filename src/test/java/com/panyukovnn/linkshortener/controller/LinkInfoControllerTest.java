package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.model.CommonRequest;
import com.panyukovnn.linkshortener.model.CommonResponse;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(expectedResponse);
        verify(linkInfoService, times(1)).createLinkInfo(createRequest);
    }

    @Test
    void shouldUpdateShortLinkSuccessfully() {
        UpdateShortLinkRequest updateRequest = UpdateShortLinkRequest.builder()
            .id(UUID.randomUUID())
            .link("abc123")
            .description("Updated description")
            .active(false)
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<UpdateShortLinkRequest> request = new CommonRequest<>();
        request.setData(updateRequest);

        LinkInfoResponse expectedResponse = LinkInfoResponse.builder()
            .id(updateRequest.getId())
            .link(updateRequest.getLink())
            .shortLink("abc123")
            .description(updateRequest.getDescription())
            .active(updateRequest.getActive())
            .endTime(updateRequest.getEndTime())
            .openingCount(5L)
            .build();

        when(linkInfoService.updateLinkInfo(any(UpdateShortLinkRequest.class)))
            .thenReturn(expectedResponse);

        CommonResponse<LinkInfoResponse> response = linkInfoController.updateShortLink(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(expectedResponse);
        verify(linkInfoService, times(1)).updateLinkInfo(updateRequest);
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

        CommonResponse<LinkInfoResponse> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(expectedResponse);
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
        assertThat(response.isSuccess()).isTrue();
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
        assertThat(response.isSuccess()).isTrue();
        verify(linkInfoService, times(1)).deleteById(id);
    }

    @Test
    void shouldFailWhenLinkIsInactive() {
        String shortLink = "abc123";
        LinkInfoResponse inactiveLinkInfo = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(shortLink)
            .active(false)
            .description("Test link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(10L)
            .build();

        when(linkInfoService.getByShortLink(shortLink))
            .thenReturn(inactiveLinkInfo);

        CommonResponse<LinkInfoResponse> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getError()).isEqualTo("Ссылка неактивна");
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

    @Test
    void shouldFailWhenLinkIsExpired() {
        String shortLink = "abc123";
        LinkInfoResponse expiredLinkInfo = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(shortLink)
            .active(true)
            .description("Test link")
            .endTime(LocalDateTime.now().minusDays(1)) // время жизни истекло
            .openingCount(10L)
            .build();

        when(linkInfoService.getByShortLink(shortLink))
            .thenReturn(expiredLinkInfo);

        CommonResponse<LinkInfoResponse> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getError()).isEqualTo("Ссылка недействительна");
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

    @Test
    void shouldSucceedWhenLinkIsActiveAndNotExpired() {
        String shortLink = "abc123";
        LinkInfoResponse validLinkInfo = LinkInfoResponse.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(shortLink)
            .active(true)
            .description("Test link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(10L)
            .build();

        when(linkInfoService.getByShortLink(shortLink))
            .thenReturn(validLinkInfo);

        CommonResponse<LinkInfoResponse> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(validLinkInfo);
        assertThat(response.getError()).isNull();
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
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

        CommonResponse<LinkInfoResponse> response = linkInfoController.getByShortLink(shortLink);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(linkWithNoEndTime);
        assertThat(response.getError()).isNull();
        verify(linkInfoService, times(1)).getByShortLink(shortLink);
    }

}
