package com.project.urlshortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.urlshortner.entity.UrlAnalytics;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {

}
