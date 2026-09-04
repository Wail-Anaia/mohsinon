-- =====================================================================
-- MOHSINON DATABASE SCHEMA - MIGRATION V3
-- Module: Identity & Profiles (User Profile & Preferences)
-- =====================================================================

CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    bio VARCHAR(500),
    avatar_url VARCHAR(255),
    phone_number VARCHAR(30),
    preferred_language VARCHAR(10) NOT NULL DEFAULT 'fr',
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    city VARCHAR(100),
    country_code VARCHAR(3),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_user_profiles_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_profiles_city ON user_profiles(city);
