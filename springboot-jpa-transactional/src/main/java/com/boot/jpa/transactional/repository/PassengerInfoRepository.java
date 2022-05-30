package com.boot.jpa.transactional.repository;

import com.boot.jpa.transactional.entity.PassengerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerInfoRepository extends JpaRepository<PassengerInfo, Long> {
}
