package com.safenews.api.service;

import com.safenews.api.dto.SourcePatchDTO;
import com.safenews.api.dto.SourceRequestDTO;
import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.exception.DuplicateResourceException;
import com.safenews.api.exception.ResourceNotFoundException;
import com.safenews.api.model.Language;
import com.safenews.api.model.Source;
import com.safenews.api.repository.SourceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SourceServiceTest {

    @Mock
    private SourceRepository sourceRepository;

    @InjectMocks
    private SourceService sourceService;

    @Test
    @DisplayName("Should create a source when URL is not duplicated")
    void createSourceWithSuccess() {
        SourceRequestDTO dto = new SourceRequestDTO(
                "Example Feed", Language.EN_US, "https://example-rss-feed.com/rss.xml", true, "Example Feed"
        );
        Source savedSource = new Source(dto);
        savedSource.setId(UUID.randomUUID());

        when(sourceRepository.existsByUrl(dto.url())).thenReturn(false);
        when(sourceRepository.save(any(Source.class))).thenReturn(savedSource);

        SourceResponseDTO response = sourceService.create(dto);

        assertNotNull(response);
        assertEquals(dto.name(), response.name());
        verify(sourceRepository, times(1)).save(any(Source.class));
    }

    @Test
    @DisplayName("Should not create a source when URL is duplicated")
    void createSourceWithDuplicatedUrl() {
        SourceRequestDTO dto = new SourceRequestDTO(
                "Example Feed", Language.EN_US, "https://example-rss-feed.com/rss.xml", true, "Example Feed"
        );

        when(sourceRepository.existsByUrl(dto.url())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> sourceService.create(dto));
        verify(sourceRepository, never()).save(any(Source.class));
    }

    @Test
    @DisplayName("Should return filtered sources when active parameter is provided")
    void getAllWithActiveFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Source source = new Source();
        source.setName("Active Source");
        Page<Source> page = new PageImpl<>(List.of(source));

        when(sourceRepository.findByActive(true, pageable)).thenReturn(page);

        Page<SourceResponseDTO> result = sourceService.getAll(true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(sourceRepository, times(1)).findByActive(true, pageable);
        verify(sourceRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return all sources when active parameter is null")
    void getAllWithoutFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Source source = new Source();
        source.setName("Any Source");
        Page<Source> page = new PageImpl<>(List.of(source));

        when(sourceRepository.findAll(pageable)).thenReturn(page);

        Page<SourceResponseDTO> result = sourceService.getAll(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(sourceRepository, times(1)).findAll(pageable);
        verify(sourceRepository, never()).findByActive(anyBoolean(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should get a source by id with success")
    void getSourceByIdSuccess() {
        UUID id = UUID.randomUUID();
        Source source = new Source();
        source.setId(id);
        source.setName("Found Source");

        when(sourceRepository.findById(id)).thenReturn(Optional.of(source));

        SourceResponseDTO response = sourceService.getById(id);

        assertNotNull(response);
        assertEquals("Found Source", response.name());
        verify(sourceRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getSourceById finds nothing")
    void getSourceByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(sourceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sourceService.getById(id));
    }

    @Test
    @DisplayName("Should update a source successfully when data is valid and URL is unique")
    void updateSourceWithSuccess() {
        UUID id = UUID.randomUUID();
        Source existingSource = new Source();
        existingSource.setId(id);
        existingSource.setUrl("https://old-url.com");
        existingSource.setName("Old Name");

        SourcePatchDTO patchDto = mock(SourcePatchDTO.class);

        doReturn(JsonNullable.of("https://new-url.com")).when(patchDto).url();
        doReturn(JsonNullable.of("New Name")).when(patchDto).name();

        doReturn(JsonNullable.undefined()).when(patchDto).language();
        doReturn(JsonNullable.undefined()).when(patchDto).description();
        doReturn(JsonNullable.undefined()).when(patchDto).active();

        when(sourceRepository.findById(id)).thenReturn(Optional.of(existingSource));
        when(sourceRepository.existsByUrl("https://new-url.com")).thenReturn(false);
        when(sourceRepository.save(any(Source.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SourceResponseDTO response = sourceService.update(id, patchDto);

        assertNotNull(response);
        assertEquals("https://new-url.com", existingSource.getUrl());
        assertEquals("New Name", existingSource.getName());
        verify(sourceRepository, times(1)).save(existingSource);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when updating to an already existing URL")
    void updateSourceThrowsExceptionWhenNewUrlAlreadyExists() {
        UUID id = UUID.randomUUID();
        Source existingSource = new Source();
        existingSource.setId(id);
        existingSource.setUrl("https://old-url.com");

        SourcePatchDTO patchDto = mock(SourcePatchDTO.class);

        doReturn(JsonNullable.of("https://duplicated-url.com")).when(patchDto).url();

        when(sourceRepository.findById(id)).thenReturn(Optional.of(existingSource));
        when(sourceRepository.existsByUrl("https://duplicated-url.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> sourceService.update(id, patchDto));
        verify(sourceRepository, never()).save(any(Source.class));
    }

    @Test
    @DisplayName("Should delete a source successfully when ID exists")
    void deleteSourceWithSuccess() {
        UUID id = UUID.randomUUID();
        when(sourceRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> sourceService.delete(id));

        verify(sourceRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existing ID")
    void deleteSourceNotFound() {
        UUID id = UUID.randomUUID();
        when(sourceRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> sourceService.delete(id));
        verify(sourceRepository, never()).deleteById(any(UUID.class));
    }
}
