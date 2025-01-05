package com.panyukovnn.linkshortener.controller;

import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MaskHeadersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkInfoService linkInfoService;

    @Test
    void shouldMaskSensitiveHeaders() throws Exception {
        String shortLink = "abc123";
        String originalUrl = "http://example.com";

        LinkInfoResponse response = LinkInfoResponse.builder()
            .link(originalUrl)
            .shortLink(shortLink)
            .build();

        doReturn(response).when(linkInfoService).getByShortLink(shortLink);

        mockMvc.perform(get("/api/v1/short-link/{shortLink}", shortLink)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
                .header("Cookie", "session=abc123; user=ivan")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .header("Custom-Header", "custom-value")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isTemporaryRedirect())
            .andExpect(header().string("Location", originalUrl));

    }
}
