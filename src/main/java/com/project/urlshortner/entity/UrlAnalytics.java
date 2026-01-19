// package com.project.urlshortner.entity;

// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Entity
// @Table(name = "url_analytics")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class UrlAnalytics {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
    
// @Column(name = "url_id", nullable = false)
// private Long urlId;
    
// @Column(name = "clicked_at", nullable = false)
// private LocalDateTime clickedAt;
    
// @Column(name = "ip_address", length = 45)
// private String ipAddress;
    
// @Column(name = "user_agent", columnDefinition = "TEXT")
// private String userAgent;
    
// @Column(name = "referrer", columnDefinition = "TEXT")
// private String referrer;
    
// @Column(name = "country", length = 2)
// private String country;
    
// @PrePersist
// protected void onCreate() {
// clickedAt = LocalDateTime.now();
// }
// }