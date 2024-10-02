package com.zerobase.reservationservice.model;

import com.zerobase.reservationservice.persist.entity.ReviewEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String sellerName;
    private Long storeId;
    private String text;
    private Long reservationId;

    public static Review fromEntity(ReviewEntity reviewEntity) {
        return Review.builder()
                .id(reviewEntity.getId())
                .customerName(reviewEntity.getCustomerName())
                .sellerName(reviewEntity.getSellerName())
                .storeId(reviewEntity.getStoreId())
                .text(reviewEntity.getText())
                .reservationId(reviewEntity.getReservationId())
                .build();
    }
}
