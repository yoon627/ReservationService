package com.zerobase.reservationservice.persist;

import com.zerobase.reservationservice.model.Review;
import com.zerobase.reservationservice.persist.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<Review> getAllByStoreId(Long storeId);
}
