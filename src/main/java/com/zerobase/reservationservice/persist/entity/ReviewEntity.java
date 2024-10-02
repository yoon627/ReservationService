package com.zerobase.reservationservice.persist.entity;

import com.zerobase.reservationservice.model.Review;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "REVIEW")
@Getter
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String sellerName;
    private Long storeId;
    private String text;
    private Long reservationId;

    public ReviewEntity(Review review) {
        this.id = review.getId();
        this.customerName = review.getCustomerName();
        this.sellerName = review.getSellerName();
        this.storeId = review.getStoreId();
        this.text = review.getText();
        this.reservationId = review.getReservationId();
    }
}
