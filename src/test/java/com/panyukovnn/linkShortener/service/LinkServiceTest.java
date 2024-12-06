package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LinkServiceTest {

    public static final String COM_PANYUKOV_NN_JAVA_BASE_MENTORING = "https://github.com/PanyukovNN/java-base-mentoring";

    @Test
    void test() {
        CreateShortLinkRequest request = CreateShortLinkRequest.builder()
                .link(COM_PANYUKOV_NN_JAVA_BASE_MENTORING)
                .endTime(LocalDateTime.now().plusDays(14))
                .description("Обучение у Николая")
                .active(true)
                .build();

        LinkService service = new LinkService();
        String shortLink = service.generateShortLink(request);
        System.out.println(shortLink);
    }
}