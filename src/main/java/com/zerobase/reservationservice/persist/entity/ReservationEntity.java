package com.zerobase.reservationservice.persist.entity;

import com.zerobase.reservationservice.model.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "RESERVATION")
@Getter
@NoArgsConstructor
public class ReservationEntity {
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

    public ReservationEntity(Reservation reservation) {
        this.id = reservation.getId();
        this.storeId = reservation.getStoreId();
        this.customerName = reservation.getCustomerName();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.sellerName = reservation.getSellerName();
        this.approval = reservation.isApproval();
        this.visited = reservation.isVisited();
        this.reviewed = reservation.isReviewed();
    }
}
