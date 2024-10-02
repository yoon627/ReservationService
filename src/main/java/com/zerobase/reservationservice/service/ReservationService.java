package com.zerobase.reservationservice.service;

import com.zerobase.reservationservice.model.Reservation;
import com.zerobase.reservationservice.persist.ReservationRepository;
import com.zerobase.reservationservice.persist.entity.ReservationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation findReservation(Reservation reservation) {
        return Reservation.fromEntity(
                this.reservationRepository.findById(reservation.getId())
                        .orElseThrow(()->new RuntimeException("해당 예약이 존재하지 않습니다."))
        );
    }

    public Reservation addReservation(Reservation reservation) {
        if (!validateReservation(reservation)) {
            throw new RuntimeException("해당 시간은 이미 예약이 있습니다.");
        }
        ReservationEntity reservationEntity = this.reservationRepository.save(new ReservationEntity(reservation));
        return Reservation.fromEntity(reservationEntity);
    }

    private boolean validateReservation(Reservation reservation) {
        return this.reservationRepository.validateReservation(reservation.getStoreId(),reservation.getStartTime(),reservation.getEndTime()).isEmpty();
    }

    public Reservation updateReservation(Reservation reservation) {
        boolean exist = this.reservationRepository.existsById(reservation.getId());
        if(!exist) {
            throw new RuntimeException("해당 예약이 존재하지 않습니다.");
        }
        ReservationEntity reservationEntity = this.reservationRepository.save(new ReservationEntity(reservation));
        return Reservation.fromEntity(reservationEntity);
    }

    public void deleteReservation(Reservation request) {
        boolean exist = this.reservationRepository.existsById(request.getId());
        if(!exist) {
            throw new RuntimeException("해당 예약이 존재하지 않습니다");
        }
        this.reservationRepository.deleteById(request.getId());
    }


    public List<Reservation> getAllReservations(String sellerName) {
        return this.reservationRepository.findAllBySellerName(sellerName);
    }
}
