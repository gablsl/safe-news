CREATE TABLE articles (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     description TEXT,
     url VARCHAR(512) NOT NULL UNIQUE,
     published_at TIMESTAMPTZ NOT NULL,
     created_at TIMESTAMPTZ DEFAULT NOW(),
     language VARCHAR(20) NOT NULL,
     source_id UUID NOT NULL,
     CONSTRAINT fk_article_source FOREIGN KEY (source_id) REFERENCES sources (id) ON DELETE CASCADE
);