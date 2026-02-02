package com.project.urlshortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.urlshortner.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {

    public boolean existsByShortCode(String shortCode);
}
