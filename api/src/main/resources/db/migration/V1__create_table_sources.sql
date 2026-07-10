CREATE TABLE sources (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name varchar(255) NOT NULL,
    url VARCHAR(512) NOT NULL,
    language VARCHAR(50) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
)