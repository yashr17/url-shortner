-- API Keys table
CREATE TABLE IF NOT EXISTS api_keys (
    id BIGSERIAL PRIMARY KEY,
    api_key VARCHAR(64) NOT NULL UNIQUE,
    user_email VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rate_limit_per_hour INTEGER NOT NULL DEFAULT 100
);

CREATE INDEX IF NOT EXISTS idx_api_keys_key_active ON api_keys(api_key, is_active);

-- URLs table
CREATE TABLE IF NOT EXISTS urls (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(10) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    api_key_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    click_count BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_api_key FOREIGN KEY (api_key_id) 
        REFERENCES api_keys(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_urls_short_code ON urls(short_code) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_urls_api_key ON urls(api_key_id);
CREATE INDEX IF NOT EXISTS idx_urls_expires_at ON urls(expires_at) WHERE expires_at IS NOT NULL AND is_active = TRUE;


-- URL Analytics table
CREATE TABLE IF NOT EXISTS url_analytics (
    id BIGSERIAL PRIMARY KEY,
    url_id BIGINT NOT NULL,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    referrer TEXT,
    country VARCHAR(2),
    CONSTRAINT fk_url FOREIGN KEY (url_id) 
        REFERENCES urls(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_analytics_url_time ON url_analytics(url_id, clicked_at DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_url_country ON url_analytics(url_id, country);

-- Comments for clarity
COMMENT ON TABLE api_keys IS 'Stores API authentication keys with rate limiting config';
COMMENT ON TABLE urls IS 'Core table storing shortened URL mappings';
COMMENT ON TABLE url_analytics IS 'Tracks every click event for analytics';

COMMENT ON COLUMN urls.short_code IS 'Unique 7-character identifier (base62 encoded)';
COMMENT ON COLUMN urls.click_count IS 'Denormalized counter for quick access (updated async)';
COMMENT ON COLUMN url_analytics.country IS 'ISO 3166-1 alpha-2 country code derived from IP';

-- Insert demo API keys
INSERT INTO api_keys (api_key, user_email, rate_limit_per_hour, is_active)
SELECT 'demo_api_key_1_yck908xhjhvx1mzyj4qrxux6rup4ay4q3ebx37ui9h1gad4jw', 'demo@urlshortener.com', 1000, true
WHERE NOT EXISTS (
    SELECT 1 FROM api_keys WHERE api_key = 'demo_api_key_1_yck908xhjhvx1mzyj4qrxux6rup4ay4q3ebx37ui9h1gad4jw'
);

INSERT INTO api_keys (api_key, user_email, rate_limit_per_hour, is_active)
SELECT 'demo_api_key_2_t0rr4xh8ktniiwvpdymqvhwme054bj49d33p6r4gidm3rf6rt', 'test@urlshortener.com', 100, true
WHERE NOT EXISTS (
    SELECT 1 FROM api_keys WHERE api_key = 'demo_api_key_2_t0rr4xh8ktniiwvpdymqvhwme054bj49d33p6r4gidm3rf6rt'
);

INSERT INTO api_keys (api_key, user_email, rate_limit_per_hour, is_active)
SELECT 'demo_api_key_3_4hx4u16w7145r95vheg3684mr8zim2trzrv42407d642wjftw', 'user@urlshortener.com', 10, true
WHERE NOT EXISTS (
    SELECT 1 FROM api_keys WHERE api_key = 'demo_api_key_3_4hx4u16w7145r95vheg3684mr8zim2trzrv42407d642wjftw'
);