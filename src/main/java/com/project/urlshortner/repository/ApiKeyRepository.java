package com.project.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.urlshortner.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByApiKeyAndIsActive(String apiKey, Boolean isActive);
}
