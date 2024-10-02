package com.zerobase.reservationservice.persist;

import com.zerobase.reservationservice.persist.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByName(String name);
}
