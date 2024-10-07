package com.zerobase.reservationservice.persist.entity;

import com.zerobase.reservationservice.model.Reservation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "RESERVATION")
@Getter
@NoArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
