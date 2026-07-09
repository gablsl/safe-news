package com.safenews.api.repository;

import com.safenews.api.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {
    @Query("SELECT s FROM Source s WHERE s.status = ENABLED")
    List<Source> findEnabledSources();
}
