package com.zerobase.reservationservice.controller;

import com.zerobase.reservationservice.model.Review;
import com.zerobase.reservationservice.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 매장에 등록된 리뷰들을 확인하는 메서드
     *
     * @param storeId (매장 아이디)
     * @return 매장에 등록된 리뷰 리스트
     */
    @GetMapping
    public List<Review> getAllReviews(Long storeId) {
        return this.reviewService.getAllReviews(storeId);
    }

    /**
     * 고객(CUSTOMER)으로 등록된 회원만
     * 리뷰 등록하는 메서드
     *
     * @param request (리뷰에 대한 정보)
     * @return 리뷰에 대한 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addReview(@RequestBody Review request) {
        Review review = this.reviewService.addReview(request);
        return ResponseEntity.ok(review);
    }

    /**
     * 고객(CUSTOMER)으로 등록된 회원만
     * 리뷰 수정하는 메서드
     *
     * @param request (수정할 리뷰에 대한 정보)
     * @return 수정한 리뷰에 대한 정보
     */
    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateReview(@RequestBody Review request) {
        Review review = this.reviewService.findReview(request);
        if (!checkAuthority(review.getCustomerName())) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }
        return ResponseEntity.ok(this.reviewService.updateReview(request));
    }

    /**
     * 고객이나 점장으로 등록된 회원들만
     * 자기가 작성하거나 자기 소유 매장에 등록된 리뷰를 삭제하는 메서드
     *
     * @param request (삭제할 리뷰에 대한 정보)
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
    public void deleteReview(@RequestBody Review request) {
        Review review = this.reviewService.findReview(request);
        if (!(checkAuthority(review.getCustomerName()) || checkAuthority(review.getSellerName()))) {
            throw new RuntimeException("리뷰 삭제 권한이 없습니다.");
        }
        this.reviewService.deleteReview(request);
    }

    /**
     * 로그인된 유저가 수정,삭제할 권한이 있는지 확인하는 메서드
     *
     * @param name (확인할 이름)
     * @return 권한이 있는 경우 true
     *               없는 경우 false
     */
    public boolean checkAuthority(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return name.equals(userDetails.getUsername());
    }
}
