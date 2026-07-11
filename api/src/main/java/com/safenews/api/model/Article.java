package com.safenews.api.model;

import com.safenews.api.dto.ArticleRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@OnDelete(action = OnDeleteAction.CASCADE)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(nullable = false, name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public Article(ArticleRequestDTO dto, Source source) {
        this.source = source;
        this.title = dto.title();
        this.language = dto.language();
        this.url = dto.url();
        this.description = dto.description();
        this.publishedAt = dto.publishedAt();
    }

    public void updateFromDTO(ArticleRequestDTO dto, Source source) {
        this.title = dto.title();
        this.language = dto.language();
        this.url = dto.url();
        this.description = dto.description();
        this.publishedAt = dto.publishedAt();
        this.source = source;
    }
}
