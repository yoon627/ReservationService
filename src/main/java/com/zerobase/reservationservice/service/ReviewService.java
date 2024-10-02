package com.zerobase.reservationservice.service;

import com.zerobase.reservationservice.model.Reservation;
import com.zerobase.reservationservice.model.Review;
import com.zerobase.reservationservice.persist.ReservationRepository;
import com.zerobase.reservationservice.persist.ReviewRepository;
import com.zerobase.reservationservice.persist.entity.ReservationEntity;
import com.zerobase.reservationservice.persist.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public List<Review> getAllReviews(Long storeId) {
        return this.reviewRepository.getAllByStoreId(storeId);
    }

    public Review findReview(Review request) {
        return Review.fromEntity(this.reviewRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다.")));
    }

    public Review addReview(Review request) {
        Reservation reservation = Reservation.fromEntity(this.reservationRepository.findById(request.getReservationId())
                .orElseThrow(()->new RuntimeException("해당 예약이 존재하지 않습니다.")));
        if(reservation.isReviewed()){
            throw new RuntimeException("해당 가게 리뷰는 이미 작성되었습니다.");
        }
        reservation.setReviewed(true);
        this.reservationRepository.save(new ReservationEntity(reservation));
        return Review.fromEntity(this.reviewRepository.save(new ReviewEntity(request)));
    }

    public void deleteReview(Review request) {
        boolean exist = this.reviewRepository.existsById(request.getId());
        if (!exist) {
            throw new RuntimeException("해당 리뷰가 존재하지 않습니다.");
        }
        this.reviewRepository.deleteById(request.getId());
    }

    public Review updateReview(Review request) {
        boolean exist = this.reviewRepository.existsById(request.getId());
        if (!exist) {
            throw new RuntimeException("해당 리뷰가 존재하지 않습니다.");
        }
        return Review.fromEntity(this.reviewRepository.save(new ReviewEntity(request)));
    }
}
