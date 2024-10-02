package com.zerobase.reservationservice.model;

import com.zerobase.reservationservice.persist.entity.ReservationEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long storeId;
    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String sellerName;
    private boolean approval;
    private boolean visited;
    private boolean reviewed;

    public static Reservation fromEntity(ReservationEntity reservationEntity) {
        return Reservation.builder()
                .id(reservationEntity.getId())
                .storeId(reservationEntity.getStoreId())
                .customerName(reservationEntity.getCustomerName())
                .startTime(reservationEntity.getStartTime())
                .endTime(reservationEntity.getEndTime())
                .sellerName(reservationEntity.getSellerName())
                .approval(reservationEntity.isApproval())
                .visited(reservationEntity.isVisited())
                .reviewed(reservationEntity.isReviewed())
                .build();
    }
}
