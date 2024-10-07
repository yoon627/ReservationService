package com.zerobase.reservationservice.controller;

import com.zerobase.reservationservice.model.Reservation;
import com.zerobase.reservationservice.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private ReservationService reservationService;

    /**
     * 고객(CUSTOMER)으로 등록된 회원만
     * 매장에 예약을 하는 메서드
     *
     * @param request (예약 정보가 담긴 요청)
     * @return 예약 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.addReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 자기 소유의 매장에 들어온 예약을 승인하는 메서드
     *
     * @param request (승인할 예약에 대한 정보)
     * @return 승인된 예약 정보
     */
    @PutMapping("/approval")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> approveReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        if (!checkAuthority(request.getSellerName())) {
            throw new RuntimeException("예약 승인 권한이 없습니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 고객(CUSTOMER)으로 등록된 회원만
     * 본인이 등록한 예약을 취소하는 메서드
     *
     * @param request
     * @return
     */
    @DeleteMapping("/cancellation")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> cancelReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        if (!checkAuthority(request.getCustomerName())) {
            throw new RuntimeException("예약 취소 권한이 없습니다.");
        }
        this.reservationService.deleteReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 자기 소유의 매장에 들어온 예약을 거절하는 메서드
     *
     * @param request (거절할 예약에 대한 정보)
     * @return 거절된 예약 정보
     */
    @PutMapping("/rejection")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> rejectReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        if (!checkAuthority(request.getSellerName())) {
            throw new RuntimeException("예약 거절 권한이 없습니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 자기 소유의 매장에 들어온 손님의 방문 여부를 처리하는 메서드
     *
     * @param request (방문된 예약에 대한 정보)
     * @return 방문된 예약에 대한 정보
     */
    @PutMapping("/visit")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> visitReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        if (!checkAuthority(request.getSellerName())) {
            throw new RuntimeException("방문 확인 권한이 없습니다.");
        }
        if (!(LocalDateTime.now().plusMinutes(10).isAfter(request.getStartTime()) && LocalDateTime.now().isBefore(request.getEndTime()))) {
            throw new RuntimeException("예약 시간 10분전부터 입장 가능합니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 자기 소유의 매장들의 예약 정보들을 확인하는 메서드
     *
     * @param sellerName (점장 이름)
     * @return 점장 소유의 매장들의 예약 정보 리스트
     */
    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public List<Reservation> getAllReservations(@RequestBody String sellerName) {
        if (!checkAuthority(sellerName)) {
            throw new RuntimeException("예약 불러오기 권한이 없습니다.");
        }
        return this.reservationService.getAllReservations(sellerName);
    }

    /**
     * 이름을 비교해 권한을 확인하는 메서드
     *
     * @return 권한이 있으면 true
     *              없으면 false
     */
    public boolean checkAuthority(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername().equals(name);
    }
}
