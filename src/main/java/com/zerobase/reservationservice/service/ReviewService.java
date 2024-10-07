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

    /**
     * 매장의 모든 리뷰를 불러오는 메서드
     *
     * @param storeId (매장 아이디)
     * @return 매장의 모든 리뷰 리스트
     */
    public List<Review> getAllReviews(Long storeId) {
        return this.reviewRepository.getAllByStoreId(storeId);
    }

    /**
     * 리뷰를 조회하는 메서드
     *
     * @param request (조회할 리뷰에 대한 정보)
     * @return 조회된 리뷰
     */
    public Review findReview(Review request) {
        return Review.fromEntity(this.reviewRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다.")));
    }

    /**
     * 예약 정보를 확인해 리뷰를 등록하는 메서드
     *
     * @param request (등록할 리뷰 정보)
     * @return 등록된 리뷰
     */
    public Review addReview(Review request) {
        Reservation reservation = Reservation.fromEntity(this.reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("해당 예약이 존재하지 않습니다.")));
        if (reservation.isReviewed()) {
            throw new RuntimeException("해당 가게 리뷰는 이미 작성되었습니다.");
        }
        reservation.setReviewed(true);
        this.reservationRepository.save(new ReservationEntity(reservation));
        return Review.fromEntity(this.reviewRepository.save(new ReviewEntity(request)));
    }

    /**
     * 리뷰를 삭제하는 메서드
     *
     * @param request (삭제할 리뷰 정보)
     */
    public void deleteReview(Review request) {
        boolean exist = this.reviewRepository.existsById(request.getId());
        if (!exist) {
            throw new RuntimeException("해당 리뷰가 존재하지 않습니다.");
        }
        this.reviewRepository.deleteById(request.getId());
    }

    /**
     * 리뷰를 수정하는 메서드
     *
     * @param request (수정할 리뷰 정보)
     * @return 수정된 리뷰
     */
    public Review updateReview(Review request) {
        boolean exist = this.reviewRepository.existsById(request.getId());
        if (!exist) {
            throw new RuntimeException("해당 리뷰가 존재하지 않습니다.");
        }
        return Review.fromEntity(this.reviewRepository.save(new ReviewEntity(request)));
    }
}
