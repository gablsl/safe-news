package com.safenews.api.repository;

import com.safenews.api.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
    
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findByUrl(String url);

    @Modifying
    @Transactional
    @Query("DELETE FROM Article a WHERE a.publishedAt < :thresholdDate")
    void deleteArticleOlderthan(OffsetDateTime thresholdDate);
}
