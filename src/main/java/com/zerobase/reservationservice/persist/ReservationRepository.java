package com.zerobase.reservationservice.persist;

import com.zerobase.reservationservice.model.Reservation;
import com.zerobase.reservationservice.persist.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    //TODO 아마 안될테니 수정해야댐
    @Query("select r from RESERVATION r where (r.startTime < :startTime and r.endTime > :startTime ) or (r.startTime < :endTime and r.endTime > :endTime ) and r.approval")
    Optional<ReservationEntity> validateReservation(@Param("storeId") Long storeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Reservation> findAllBySellerName(String sellerName);
}
