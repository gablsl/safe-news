package com.safenews.api.service;

import com.safenews.api.dto.ArticleRequestDTO;
import com.safenews.api.dto.ArticleResponseDTO;
import com.safenews.api.exception.ResourceNotFoundException;
import com.safenews.api.exception.SourceNotAvailableException;
import com.safenews.api.model.Article;
import com.safenews.api.model.Source;
import com.safenews.api.repository.ArticleRepository;
import com.safenews.api.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final SourceRepository sourceRepository;

    @Transactional
    public ArticleResponseDTO create(ArticleRequestDTO dto) {
        Source source = sourceRepository.findById(dto.sourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID " + dto.sourceId()));

        if (!source.isActive()) {
            throw new SourceNotAvailableException("You can't insert articles in an inactive source");
        }

        Optional<Article> existingArticle = articleRepository.findByUrl(dto.url());

        if (existingArticle.isPresent()) {
            Article articleToUpdate = existingArticle.get();
            articleToUpdate.updateFromDTO(dto, source);
            return new ArticleResponseDTO(articleRepository.save(articleToUpdate));
        }

        Article newArticle = new Article(dto, source);

        return new ArticleResponseDTO(articleRepository.save(newArticle));
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getAll(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(ArticleResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public ArticleResponseDTO getById(UUID id) {
        return articleRepository.findById(id)
                .map(ArticleResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID " + id));
    }

    @Transactional
    public void delete(UUID id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article not found with ID " + id);
        }

        articleRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupOldArticles() {
        log.info("Starting the auto clean of old articles");

        OffsetDateTime retentionLimit = OffsetDateTime.now().minusDays(7);

        articleRepository.deleteArticleOlderthan(retentionLimit);

        log.info("Cleaning completed successfully");
    }
}
