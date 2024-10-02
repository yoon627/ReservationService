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

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.addReservation(request);
        return ResponseEntity.ok(reservation);
    }

    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> approveReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getSellerName())) {
            throw new RuntimeException("예약 승인 권한이 없습니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 고객이 예약을 취소하는 경우
     *
     * @param request
     * @return
     */
    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> cancelReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getCustomerName())) {
            throw new RuntimeException("예약 취소 권한이 없습니다.");
        }
        this.reservationService.deleteReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 점장이 예약을 거절하는 경우
     *
     * @param request
     * @return
     */
    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> rejectReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getSellerName())) {
            throw new RuntimeException("예약 거절 권한이 없습니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 예약 후 방문했는지 확인하는 경우
     */
    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> visitReservation(@RequestBody Reservation request) {
        Reservation reservation = this.reservationService.findReservation(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getSellerName())) {
            throw new RuntimeException("방문 확인 권한이 없습니다.");
        }
        if (!(LocalDateTime.now().plusMinutes(10).isAfter(request.getStartTime()) && LocalDateTime.now().isBefore(request.getEndTime()))) {
            throw new RuntimeException("예약 시간 10분전부터 입장 가능합니다.");
        }
        Reservation result = this.reservationService.updateReservation(request);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public List<Reservation> getAllReservations(@RequestBody String sellerName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!sellerName.equals(userDetails.getUsername())) {
            throw new RuntimeException("예약 불러오기 권한이 없습니다.");
        }
        return this.reservationService.getAllReservations(sellerName);
    }
}
