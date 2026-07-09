package com.safenews.api.service;

import com.safenews.api.dto.SourceRequestDTO;
import com.safenews.api.dto.SourceResponseDTO;
import com.safenews.api.model.Source;
import com.safenews.api.repository.SourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

    @Transactional
    public SourceResponseDTO create(SourceRequestDTO sourceRequestDTO) {
        return new SourceResponseDTO(sourceRepository.save(new Source(sourceRequestDTO)));
    }

    @Transactional
    public List<SourceResponseDTO> getAll() {
        return sourceRepository.findAll().stream()
                .map(SourceResponseDTO::new)
                .toList();
    }

    @Transactional
    public Optional<SourceResponseDTO> getById(UUID id) {
        return sourceRepository.findById(id)
                .map(SourceResponseDTO::new);
    }

    @Transactional
    public List<SourceResponseDTO> getEnabledSources() {
        return sourceRepository.findActiveSources().stream()
                .map(SourceResponseDTO::new)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        sourceRepository.deleteById(id);
    }
}
