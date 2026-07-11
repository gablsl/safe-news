package com.safenews.api.repository;

import com.safenews.api.model.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {
    Page<Source> findByActive(boolean active, Pageable pageable);
    boolean existsByUrl(String url);
}
