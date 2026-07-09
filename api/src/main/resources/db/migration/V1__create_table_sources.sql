CREATE TABLE sources (
    id UUID DEFAULT gen_random_uuid() primary key,
    name varchar(255) NOT NULL,
    language VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
)