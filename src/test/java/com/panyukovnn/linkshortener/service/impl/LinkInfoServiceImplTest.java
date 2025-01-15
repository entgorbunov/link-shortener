package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.PageableRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
@SpringBootTest
@TestPropertySource(properties = {
    "link-shortener.short-link-length=8",
    "link-shortener.logging.enable-log-execution-time=true"
})
public class LinkInfoServiceImplTest {

    @MockBean
    private LinkInfoRepository linkInfoRepository;

    @Autowired
    private LinkInfoProperty linkInfoProperty;

    @Autowired
    private LinkInfoService linkInfoService;

    @Test
    void shouldReturnLinkInfoWhenShortLinkExists() {
        LinkInfo linkInfo = LinkInfo.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .active(true)
            .description("Example link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(0L)
            .build();
        when(linkInfoRepository.findActiveShortLink(
            eq(linkInfo.getShortLink()), any(LocalDateTime.class)))
            .thenReturn(Optional.of(linkInfo));


        Optional<LinkInfoResponse> response = Optional.ofNullable(linkInfoService.getByShortLink(linkInfo.getShortLink()));

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(linkInfo.getShortLink(), response.orElseThrow(() -> new NotFoundException("ShortLink hasn't found")).getShortLink()),
            () -> assertEquals(linkInfo.getLink(), response.orElseThrow(() -> new NotFoundException("Link hasn't found")).getLink()),
            () -> assertEquals(linkInfo.getActive(), response.orElseThrow(() -> new NotFoundException("Status hasn't found")).getActive()),
            () -> assertEquals(linkInfo.getDescription(), response.orElseThrow(() -> new NotFoundException("Description hasn't found")).getDescription()),
            () -> assertEquals(linkInfo.getEndTime(), response.orElseThrow(() -> new NotFoundException("EndTime hasn't found")).getEndTime()),
            () -> assertEquals(linkInfo.getOpeningCount(), response.orElseThrow(() -> new NotFoundException("OpeningCount hasn't found")).getOpeningCount())
        );
    }

    @Test
    void shouldThrowNotFoundExceptionWhenShortLinkDoesNotExist() {
        String shortLink = "nonexistent";
        when(linkInfoRepository.findActiveShortLink(eq(shortLink), any(LocalDateTime.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkInfoService.getByShortLink(shortLink))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Ссылка nonexistent не найдена");
    }

    @Test
    void shouldReturnAllExistingLinks() {
        LinkInfo linkInfo = LinkInfo.builder()
            .id(UUID.randomUUID())
            .link("http://google.com")
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .active(true)
            .description("Google link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(0L)
            .build();

        LinkInfo linkInfo1 = LinkInfo.builder()
            .id(UUID.randomUUID())
            .link("http://yandex.com")
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .active(true)
            .description("Yandex link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(0L)
            .build();

        LinkInfo linkInfo2 = LinkInfo.builder()
            .id(UUID.randomUUID())
            .link("http://example.com")
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .active(true)
            .description("Example link")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(0L)
            .build();

        List<LinkInfo> sourceList = List.of(linkInfo, linkInfo1, linkInfo2);
        PageImpl<LinkInfo> sourcePage = new PageImpl<>(sourceList);

        FilterLinkInfoRequest filterLinkInfoRequest = FilterLinkInfoRequest.builder()
            .linkPart(null)
            .endTimeFrom(null)
            .endTimeTo(null)
            .descriptionPart(null)
            .active(null)
            .page(PageableRequest.builder().number(1).size(10).build())
            .build();

        when(linkInfoRepository.findByFilter(
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            any(Pageable.class)
        )).thenReturn(sourcePage);

        List<LinkInfoResponse> resultList = linkInfoService.findByFilter(filterLinkInfoRequest);

        assertAll(
            () -> assertNotNull(resultList),
            () -> assertEquals(sourceList.size(), resultList.size())
        );

        for (int i = 0; i < sourcePage.getSize(); i++) {
            LinkInfo source = sourcePage.getContent().get(i);
            LinkInfoResponse result = resultList.get(i);

            assertAll(
                () -> assertEquals(source.getId(), result.getId()),
                () -> assertEquals(source.getLink(), result.getLink()),
                () -> assertEquals(source.getShortLink(), result.getShortLink()),
                () -> assertEquals(source.getActive(), result.getActive()),
                () -> assertEquals(source.getEndTime(), result.getEndTime()),
                () -> assertEquals(source.getOpeningCount(), result.getOpeningCount()),
                () -> assertEquals(source.getDescription(), result.getDescription())
            );
        }

        verify(linkInfoRepository, times(1)).findByFilter(
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            any(Pageable.class)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoLinksExist() {
        FilterLinkInfoRequest filterLinkInfoRequest = new FilterLinkInfoRequest();

        PageImpl<LinkInfo> emptyPage = new PageImpl<>(new ArrayList<>());

        when(linkInfoRepository.findByFilter(
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            any(Pageable.class)
        )).thenReturn(emptyPage);

        List<LinkInfoResponse> emptyResult = linkInfoService.findByFilter(filterLinkInfoRequest);

        assertThat(emptyResult).isNotNull();
        assertThat(emptyResult).isEmpty();

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

        LinkInfo savedLinkInfo = LinkInfo.builder()
            .id(UUID.randomUUID())
            .link(request.getLink())
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .active(request.getActive())
            .description(request.getDescription())
            .endTime(request.getEndTime())
            .openingCount(0L)
            .build();

        when(linkInfoRepository.save(any(LinkInfo.class))).thenReturn(savedLinkInfo);

        LinkInfoResponse response = linkInfoService.createLinkInfo(request);

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(request.getLink(), response.getLink()),
            () -> assertEquals(request.getDescription(), response.getDescription()),
            () -> assertEquals(request.getActive(), response.getActive()),
            () -> assertEquals(request.getEndTime(), response.getEndTime()),
            () -> assertNotNull(response.getId())
        );


        verify(linkInfoRepository, times(1)).save(any(LinkInfo.class));

    }

    @Test
    void shouldCreateDifferentShortLinksForSameRequest() {

        CreateShortLinkRequest request = CreateShortLinkRequest
            .builder()
            .link("Google.com")
            .active(true)
            .description("Google")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        when(linkInfoRepository.save(any(LinkInfo.class))).thenAnswer(invocation -> invocation.<LinkInfo>getArgument(0));

        LinkInfoResponse response1 = linkInfoService.createLinkInfo(request);
        LinkInfoResponse response2 = linkInfoService.createLinkInfo(request);
        LinkInfoResponse response3 = linkInfoService.createLinkInfo(request);

        assertAll(
            () -> assertNotEquals(response1.getShortLink(), response2.getShortLink()),
            () -> assertNotEquals(response2.getShortLink(), response3.getShortLink()),
            () -> assertNotEquals(response1.getShortLink(), response3.getShortLink()),
            () -> assertEquals(request.getLink(), response1.getLink()),
            () -> assertEquals(request.getDescription(), response1.getDescription()),
            () -> assertEquals(request.getActive(), response1.getActive()),
            () -> assertEquals(request.getEndTime(), response1.getEndTime())
        );
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
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .openingCount(0L)
            .build();

        when(linkInfoRepository.save(any(LinkInfo.class))).thenReturn(expectedSavedLink);

        LinkInfoResponse response = linkInfoService.createLinkInfo(request);

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(request.getLink(), response.getLink()),
            () -> assertEquals(request.getDescription(), response.getDescription()),
            () -> assertEquals(request.getActive(), response.getActive()),
            () -> assertEquals(request.getEndTime(), response.getEndTime()),
            () -> assertNotNull(response.getId()),
            () -> assertEquals(8, response.getShortLink().length()),
            () -> assertEquals(0L, response.getOpeningCount()),
            () -> assertNotNull(response.getShortLink())
        );

        verify(linkInfoRepository, times(1)).save(any(LinkInfo.class));

    }

    @Test
    void shouldSuccessfulUpdateLinkInfo() {
        UUID id = UUID.randomUUID();

        LinkInfo existingLink = LinkInfo.builder()
            .id(id)
            .link("http://example.com")
            .shortLink("abc123")
            .active(true)
            .description("Original description")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(5L)
            .build();

        doReturn(Optional.ofNullable(existingLink)).when(linkInfoRepository).findById(id);
        when(linkInfoRepository.save(any(LinkInfo.class))).thenAnswer(i -> i.getArgument(0));

        UpdateShortLinkRequest request = UpdateShortLinkRequest.builder()
            .id(id.toString())
            .link("abc123")
            .description("Updated description")
            .active(false)
            .build();

        LinkInfoResponse response = linkInfoService.updateLinkInfo(request);

        assertNotNull(existingLink, "Существующая ссылка не должна быть null");
        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(existingLink.getId(), response.getId()),
            () -> assertEquals("Updated description", response.getDescription()),
            () -> assertEquals(false, response.getActive()),
            () -> assertEquals(existingLink.getLink(), response.getLink()),
            () -> assertEquals(existingLink.getShortLink(), response.getShortLink()),
            () -> assertEquals(existingLink.getEndTime(), response.getEndTime()),
            () -> assertEquals(existingLink.getOpeningCount(), response.getOpeningCount())
        );

        verify(linkInfoRepository).findById(id);
        verify(linkInfoRepository).save(any(LinkInfo.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistingLink() {
        when(linkInfoRepository.findActiveShortLink("nonexistent", LocalDateTime.now().plusDays(1))).thenReturn(Optional.empty());

        UUID id = UUID.randomUUID();

        UpdateShortLinkRequest request = UpdateShortLinkRequest.builder()
            .id(id.toString())
            .link("nonexistent")
            .description("New description")
            .active(true)
            .build();

        assertThatThrownBy(() -> linkInfoService.updateLinkInfo(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Ссылка не найдена");

        verify(linkInfoRepository).findById(id);
        verify(linkInfoRepository, never()).save(any(LinkInfo.class));
    }

    @Test
    void shouldOnlyUpdateProvidedFields() {
        UUID id = UUID.randomUUID();

        LinkInfo existingLink = LinkInfo.builder()
            .id(id)
            .link("http://example.com")
            .shortLink("abc123")
            .active(true)
            .description("Original description")
            .endTime(LocalDateTime.now().plusDays(1))
            .openingCount(5L)
            .build();

        when(linkInfoRepository.findById(id)).thenReturn(Optional.of(existingLink));
        when(linkInfoRepository.save(any(LinkInfo.class))).thenAnswer(i -> i.getArgument(0));

        UpdateShortLinkRequest request = UpdateShortLinkRequest.builder()
            .id(id.toString())
            .link("abc123")
            .description("Updated description")
            .build();

        LinkInfoResponse response = linkInfoService.updateLinkInfo(request);

        assertNotNull(existingLink, "Существующая ссылка не должна быть равна null");
        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals("Updated description", response.getDescription()),
            () -> assertEquals(existingLink.getActive(), response.getActive()),
            () -> assertEquals(existingLink.getLink(), response.getLink()),
            () -> assertEquals(existingLink.getEndTime(), response.getEndTime()),
            () -> assertEquals(existingLink.getOpeningCount(), response.getOpeningCount())
        );

        verify(linkInfoRepository).findById(id);
        verify(linkInfoRepository).save(any(LinkInfo.class));
    }
}
