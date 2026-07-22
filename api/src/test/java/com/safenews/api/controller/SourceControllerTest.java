package com.safenews.api.controller;

import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.model.Language;
import com.safenews.api.service.SourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.;


import java.time.OffsetDateTime;
import java.util.UUID;

@WebMvcTest(SourceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SourceService sourceService;

    @Test
    @DisplayName("Should return 201 Created and the Source when creation is successful")
    void createShouldReturnCreated() throws Exception {
        String requestBody = """
                {
                    "name": "TechCrunch",
                    "language": "EN_US",
                    "url": "https://techcrunch.com/feed",
                    "active": true,
                    "description": "Technology news"
                }
                """;

        UUID mockId = UUID.randomUUID();
        SourceResponseDTO responseDto = new SourceResponseDTO(
                mockId,
                "TechCrunch",
                Language.EN_US,
                "https://techcrunch.com/feed",
                true,
                "Technology news",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        when(sourceService.create(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated()) // Valida se retornou HTTP 201
                .andExpect(jsonPath("$.id").value(mockId.toString())) // Valida campos do JSON retornado
                .andExpect(jsonPath("$.name").value("TechCrunch"))
                .andExpect(jsonPath("$.url").value("https://techcrunch.com/feed"));
    }
}
