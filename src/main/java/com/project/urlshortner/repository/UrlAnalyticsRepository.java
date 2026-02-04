package com.project.urlshortner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.urlshortner.entity.UrlAnalytics;

import io.lettuce.core.dynamic.annotation.Param;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {

    List<UrlAnalytics> findTop10ByUrlIdOrderByClickedAtDesc(Long id);

    @Query(value = "SELECT DATE(clicked_at) AS click_date, COUNT(id) AS click_count FROM url_analytics WHERE url_id = :id GROUP BY DATE(clicked_at) ORDER BY 1 DESC", nativeQuery = true)
    List<Object[]> getClicksByDate(@Param("id") Long id);

    @Query("SELECT ua.country as country, COUNT(ua.id) as clickCount FROM UrlAnalytics ua WHERE ua.urlId = :id GROUP BY ua.country ORDER BY clickCount DESC")
    List<Object[]> getClicksByCountry(Long id);
}
