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

    /**
     * 예약을 찾아오는 메서드
     *
     * @param reservation (찾을 예약 정보)
     * @return 찾은 예약 정보
     */
    public Reservation findReservation(Reservation reservation) {
        return Reservation.fromEntity(
                this.reservationRepository.findById(reservation.getId())
                        .orElseThrow(() -> new RuntimeException("해당 예약이 존재하지 않습니다."))
        );
    }

    /**
     * 예약을 등록하는 메서드
     *
     * @param reservation (등록할 예약 정보)
     * @return 등록된 예약 정보
     */
    public Reservation addReservation(Reservation reservation) {
        if (!validateReservation(reservation)) {
            throw new RuntimeException("해당 시간은 이미 예약이 있습니다.");
        }
        ReservationEntity reservationEntity = this.reservationRepository.save(new ReservationEntity(reservation));
        return Reservation.fromEntity(reservationEntity);
    }

    /**
     * 예약이 가능한지 검증하는 메서드
     *
     * @param reservation (검증할 예약 정보)
     * @return 예약이 가능한 시간일 경우 true
     *                      아닐 경우 false
     */
    private boolean validateReservation(Reservation reservation) {
        return this.reservationRepository.validateReservation(reservation.getStoreId(), reservation.getStartTime(), reservation.getEndTime()).isEmpty();
    }

    /**
     * 예약을 수정하는 메서드
     *
     * @param reservation (수정할 예약 정보)
     * @return 수정된 예약 정보
     */
    public Reservation updateReservation(Reservation reservation) {
        boolean exist = this.reservationRepository.existsById(reservation.getId());
        if (!exist) {
            throw new RuntimeException("해당 예약이 존재하지 않습니다.");
        }
        ReservationEntity reservationEntity = this.reservationRepository.save(new ReservationEntity(reservation));
        return Reservation.fromEntity(reservationEntity);
    }

    /**
     * 예약을 삭제할 메서드
     *
     * @param request (삭제할 예약 정보)
     */
    public void deleteReservation(Reservation request) {
        boolean exist = this.reservationRepository.existsById(request.getId());
        if (!exist) {
            throw new RuntimeException("해당 예약이 존재하지 않습니다");
        }
        this.reservationRepository.deleteById(request.getId());
    }

    /**
     * 점장이 가진 매장들의 예약 정보 리스트 불러오는 메서드
     *
     * @param sellerName (점장 이름)
     * @return 예약 정보 리스트
     */
    public List<Reservation> getAllReservations(String sellerName) {
        return this.reservationRepository.findAllBySellerName(sellerName);
    }
}
