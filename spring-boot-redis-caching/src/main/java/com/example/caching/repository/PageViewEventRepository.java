package com.example.caching.repository;

import com.example.caching.domain.PageViewEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewEventRepository extends JpaRepository<PageViewEvent, Long> {
}
