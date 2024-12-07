package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkInfoServiceImplTest {

    @Mock
    private LinkInfoRepository repository;

    private com.panyukovnn.linkshortener.service.impl.LinkInfoServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new com.panyukovnn.linkshortener.service.impl.LinkInfoServiceImpl(repository);
    }

    @Test
    void shouldReturnLinkInfoWhenShortLinkExists() {

        LinkInfo linkInfo = LinkInfo.builder()
                                    .id(UUID.randomUUID())
                                    .link("http://example.com")
                                    .shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
                                    .active(true)
                                    .description("Example link")
                                    .endTime(LocalDateTime.now().plusDays(1))
                                    .openingCount(0L)
                                    .build();
        when(repository.findByShortLink(linkInfo.getShortLink())).thenReturn(linkInfo);

        LinkInfoResponse response = service.getByShortLink(linkInfo.getShortLink());

        assertThat(response).isNotNull();
        assertThat(response.getShortLink()).isEqualTo(linkInfo.getShortLink());
        assertThat(response.getLink()).isEqualTo(linkInfo.getLink());
        assertThat(response.getActive()).isEqualTo(linkInfo.getActive());
        assertThat(response.getDescription()).isEqualTo(linkInfo.getDescription());
        assertThat(response.getEndTime()).isEqualTo(linkInfo.getEndTime());
        assertThat(response.getOpeningCount()).isEqualTo(linkInfo.getOpeningCount());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenShortLinkDoesNotExist() {

        String shortLink = "nonexistent";
        when(repository.findByShortLink(shortLink)).thenReturn(null);

        assertThatThrownBy(() -> service.getByShortLink(shortLink))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Link not found: " + shortLink);
    }

    @Test
    void shouldReturnAllExistingLinks() {

        LinkInfo linkInfo = LinkInfo.builder()
                                    .id(UUID.randomUUID())
                                    .link("http://google.com")
                                    .shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
                                    .active(true)
                                    .description("Google link")
                                    .endTime(LocalDateTime.now().plusDays(1))
                                    .openingCount(0L)
                                    .build();

        LinkInfo linkInfo1 = LinkInfo.builder()
                                     .id(UUID.randomUUID())
                                     .link("http://yandex.com")
                                     .shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
                                     .active(true)
                                     .description("Yandex link")
                                     .endTime(LocalDateTime.now().plusDays(1))
                                     .openingCount(0L)
                                     .build();

        LinkInfo linkInfo2 = LinkInfo.builder()
                                     .id(UUID.randomUUID())
                                     .link("http://example.com")
                                     .shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
                                     .active(true)
                                     .description("Example link")
                                     .endTime(LocalDateTime.now().plusDays(1))
                                     .openingCount(0L)
                                     .build();

        List<LinkInfo> sourceList = List.of(linkInfo, linkInfo1, linkInfo2);

        when(repository.findAll()).thenReturn(sourceList);

        List<LinkInfoResponse> resultList = service.findByFilter();

        for (int i = 0; i < sourceList.size(); i++) {
            LinkInfo source = sourceList.get(i);
            LinkInfoResponse result = resultList.get(i);

            assertThat(result.getId()).isEqualTo(source.getId());
            assertThat(result.getLink()).isEqualTo(source.getLink());
            assertThat(result.getShortLink()).isEqualTo(source.getShortLink());
            assertThat(result.getActive()).isEqualTo(source.getActive());
            assertThat(result.getEndTime()).isEqualTo(source.getEndTime());
            assertThat(result.getOpeningCount()).isEqualTo(source.getOpeningCount());
            assertThat(result.getDescription()).isEqualTo(source.getDescription());
        }

        verify(repository, times(1)).findAll();

    }

    @Test
    void shouldReturnEmptyListWhenNoLinksExist() {

        when(repository.findAll()).thenReturn(new ArrayList<>());
        List<LinkInfoResponse> emptyResult = service.findByFilter();

        assertThat(emptyResult).isNotNull().hasSize(0);

    }

    @Test
    void shouldCreateLinkInfoWithCorrectFields() {
        CreateShortLinkRequest request = CreateShortLinkRequest
                .builder()
                .link("Google.com")
                .active(true)
                .description("Google")
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        when(repository.save(any(LinkInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LinkInfoResponse response = service.createLinkInfo(request);

        assertThat(response).isNotNull();
        assertThat(response.getLink()).isEqualTo(request.getLink());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getActive()).isEqualTo(request.getActive());
        assertThat(response.getEndTime()).isEqualTo(request.getEndTime());
        assertThat(response.getId()).isNotNull();

        verify(repository, times(1)).save(any(LinkInfo.class));

    }

    @Test
    void shouldGenerateUniqueShortLinksForSameRequest() {

        CreateShortLinkRequest request = CreateShortLinkRequest
                .builder()
                .link("Google.com")
                .active(true)
                .description("Google")
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        when(repository.save(any(LinkInfo.class))).thenAnswer(invocation -> invocation.<LinkInfo>getArgument(0));

        LinkInfoResponse response1 = service.createLinkInfo(request);
        LinkInfoResponse response2 = service.createLinkInfo(request);
        LinkInfoResponse response3 = service.createLinkInfo(request);

        assertThat(response1.getShortLink().length()).isEqualTo(8);
        assertThat(response2.getShortLink().length()).isEqualTo(8);
        assertThat(response3.getShortLink().length()).isEqualTo(8);

        Set<LinkInfoResponse> uniqueShortLinks = Set.of(response1, response2, response3);

        assertThat(uniqueShortLinks).hasSize(3);

    }

    @Test
    void shouldSaveLinkInfoAndReturnCorrectResponse() {
        CreateShortLinkRequest request = CreateShortLinkRequest
                .builder()
                .link("Google.com")
                .active(true)
                .description("Google")
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        LinkInfo expectedSavedLink = LinkInfo.builder()
                                             .link(request.getLink())
                                             .active(request.getActive())
                                             .description(request.getDescription())
                                             .endTime(request.getEndTime())
                                             .id(UUID.randomUUID())
                                             .shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
                                             .openingCount(0L)
                                             .build();

        when(repository.save(any(LinkInfo.class))).thenReturn(expectedSavedLink);

        LinkInfoResponse response = service.createLinkInfo(request);

        assertThat(response).isNotNull();
        assertThat(response.getLink()).isEqualTo(request.getLink());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getActive()).isEqualTo(request.getActive());
        assertThat(response.getEndTime()).isEqualTo(request.getEndTime());
        assertThat(response.getId()).isNotNull();
        assertThat(response.getShortLink()).hasSize(8);
        assertThat(response.getOpeningCount()).isEqualTo(0L);
        assertThat(response.getShortLink()).isNotNull();

        verify(repository, times(1)).save(any(LinkInfo.class));

    }

}
