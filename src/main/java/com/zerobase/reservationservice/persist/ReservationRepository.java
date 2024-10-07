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

    @Query("select r from RESERVATION r where r.storeId = :storeId and ((r.startTime < :startTime and r.endTime > :startTime) or (r.startTime < :endTime and r.endTime > :endTime)) and r.approval = true")
    Optional<ReservationEntity> validateReservation(@Param("storeId") Long storeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Reservation> findAllBySellerName(String sellerName);
}
