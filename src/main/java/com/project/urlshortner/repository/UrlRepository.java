package com.project.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.urlshortner.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {

    public boolean existsByShortCode(String shortCode);

    public Optional<Url> findByShortCodeAndIsActive(String shortCode, Boolean isActive);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = ?1")
    public void incrementClickCount(String shortcode);

    public Optional<Url> findByShortCodeAndApiKey_ApiKeyAndIsActiveTrue(String shortCode, String apiKey);
}
