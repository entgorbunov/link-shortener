package com.panyukovnn.linkshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LinkControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFailWhenCreateRequestHasPastEndTime() throws Exception {

        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://google.com")
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().minusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("body.endTime"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("Дата окончания ссылки должна быть в будущем"));
    }

    @Test
    void shouldFailWhenCreateRequestHasEmptyDescription() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://example.com")
            .active(true)
            .description("")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("body.description"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("Описание не должно быть пустым"));
    }

    @Test
    void shouldFailWhenCreateRequestHasNullActive() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://example.com")
            .active(null)
            .description("Test description")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("body.active"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("Признак активности не может быть null"));
    }

    @Test
    void shouldPassWhenCreateRequestIsValid() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://example.com")
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidLinksProvider")
    void shouldFailWhenLinkIsInvalid(String link, String expectedErrorMessage) throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link(link)
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[?(@.message == '" + expectedErrorMessage + "')]").exists());
    }

    private static Stream<Arguments> invalidLinksProvider() {
        return Stream.of(
            Arguments.of(null, "Ссылка не может быть пустой"),
            Arguments.of("", "Ссылка не может быть пустой"),
            Arguments.of("invalid-url", "В ссылке допущена ошибка"),
            Arguments.of("ftp://invalid.com", "В ссылке допущена ошибка"),
            Arguments.of("http:/invalid", "В ссылке допущена ошибка")
        );
    }
}
