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

    /**
     * 상점의 이름을 통해 상점을 찾는 메서드
     *
     * @param keyword (상점 이름)
     * @return 상점에 대한 정보
     */
    @GetMapping
    public ResponseEntity<?> searchStore(@RequestParam String keyword) {
        Store result = this.storeService.searchStore(keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 상점을 추가할 수 있는 메서드
     *
     * @param request (추가할 상점에 대한 정보를 담긴 요청)
     * @return 추가된 상점에 대한 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addStore(@RequestBody Store request) {
        Store store = this.storeService.addStore(request);
        return ResponseEntity.ok(store);
    }

    /**
     * 점장(SELLER)으로 등록된 회원만
     * 자기 소유의 상점을 수정하는 메서드
     *
     * @param request (수정할 상점에 대한 정보가 담긴 요청)
     * @return 수정된 상점에 대한 정보
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> updateStore(@RequestBody Store request) {
        if (!checkAuthority(request.getSellerName())) {
            throw new RuntimeException("매장을 수정할 권한이 없습니다.");
        }
        Store store = this.storeService.updateStore(request);
        return ResponseEntity.ok(store);
    }

    /**
     * 점장으로 등록된 회원만
     * 자기 소유의 상점을 삭제하는 메서드
     *
     * @param request (삭제할 상점에 대한 정보가 담긴 요청)
     * @return 삭제된 상점에 대한 정보
     */
    @PutMapping("/delete")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteStore(@RequestBody Store request) {
        if (!checkAuthority(request.getSellerName())) {
            throw new RuntimeException("매장을 삭제할 권한이 없습니다.");
        }
        this.storeService.deleteStore(request);
        return ResponseEntity.ok(request);
    }

    /**
     * 로그인된 회원과 수정,삭제할 상점의 점장을 비교하는 메서드
     *
     * @param sellerName (수정,삭제할 상점의 점장 이름)
     * @return 회원 이름과 점장의 이름이 일치하는 경우 true
     * 일치하지 않는 경우 false
     */
    public boolean checkAuthority(String sellerName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return sellerName.equals(userDetails.getUsername());
    }
}
