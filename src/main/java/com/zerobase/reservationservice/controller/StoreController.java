package com.zerobase.reservationservice.controller;

import com.zerobase.reservationservice.model.Store;
import com.zerobase.reservationservice.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<?> searchStore(@RequestParam String keyword) {
        Store result = this.storeService.searchStore(keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addStore(@RequestBody Store request) {
        Store store = this.storeService.addStore(request);
        return ResponseEntity.ok(store);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> updateStore(@RequestBody Store request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getSellerName())) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }
        Store store = this.storeService.updateStore(request);
        return ResponseEntity.ok(store);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteStore(@RequestBody Store request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String sellerName = userDetails.getUsername();
        if (!sellerName.equals(request.getSellerName())) {
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }
        this.storeService.deleteStore(request);
        return ResponseEntity.ok(request);
    }
}
