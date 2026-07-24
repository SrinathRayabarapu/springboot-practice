package com.srinath.caching.repository;

import com.srinath.caching.domain.PageViewEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewEventRepository extends JpaRepository<PageViewEvent, Long> {
}
