package com.panyukovnn.linkshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.common.CommonRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

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
    void shouldFailWhenCreatedRequestHasEmptyLink() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("")
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("data.link"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("В ссылке допущена ошибка"));
    }

    @Test
    void shouldFailWhenCreateRequestHasInvalidLink() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("invalid-url")
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().plusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("data.link"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("В ссылке допущена ошибка"));
    }

    @Test
    void shouldFailWhenCreateRequestHasPastEndTime() throws Exception {
        CreateShortLinkRequest createRequest = CreateShortLinkRequest.builder()
            .link("http://google.com")
            .active(true)
            .description("Test description")
            .endTime(LocalDateTime.now().minusDays(1))
            .build();

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("data.endTime"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("Дата должна быть в будущем"));
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
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("data.description"))
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
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("data.active"))
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
        request.setData(createRequest);

        mockMvc.perform(post("/api/v1/links/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
