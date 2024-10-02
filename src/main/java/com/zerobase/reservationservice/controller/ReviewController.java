package com.zerobase.reservationservice.controller;

import com.zerobase.reservationservice.model.Reservation;
import com.zerobase.reservationservice.model.Review;
import com.zerobase.reservationservice.persist.ReservationRepository;
import com.zerobase.reservationservice.service.ReservationService;
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

    @GetMapping
    public List<Review> getAllReviews(Long storeId) {
        return this.reviewService.getAllReviews(storeId);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addReview(@RequestBody Review request) {
        Review review = this.reviewService.addReview(request);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateReview(@RequestBody Review request) {
        Review review = this.reviewService.findReview(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String customerName = userDetails.getUsername();
        if (!customerName.equals(review.getCustomerName())) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }
        return ResponseEntity.ok(this.reviewService.updateReview(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
    public void deleteReview(@RequestBody Review request) {
        Review review = this.reviewService.findReview(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        if (!(username.equals(review.getCustomerName()) || username.equals(review.getSellerName()))) {
            throw new RuntimeException("리뷰 삭제 권한이 없습니다.");
        }
        this.reviewService.deleteReview(request);
    }
}
