package com.zerobase.reservationservice.service;

import com.zerobase.reservationservice.model.Store;
import com.zerobase.reservationservice.persist.StoreRepository;
import com.zerobase.reservationservice.persist.entity.StoreEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 매장을 검색하는 메서드
     *
     * @param name (매장 이름)
     * @return 매장 정보
     */
    public Store searchStore(String name) {
        StoreEntity store = this.storeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("해당 상점이 존재하지 않습니다."));
        return Store.fromEntity(store);
    }

    /**
     * 매장을 등록하는 메서드
     *
     * @param store (등록할 매장 정보)
     * @return 등록된 매장 정보
     */
    public Store addStore(Store store) {
        boolean exists = this.storeRepository.existsById(store.getId());
        if (exists) {
            throw new RuntimeException("이미 존재하는 상점입니다.");
        }
        StoreEntity storeEntity = this.storeRepository.save(new StoreEntity(store));
        return Store.fromEntity(storeEntity);
    }

    /**
     * 매장을 수정하는 메서드
     *
     * @param store (수정할 매장 정보)
     * @return 수정된 매장 정보
     */
    public Store updateStore(Store store) {
        boolean exists = this.storeRepository.existsById(store.getId());
        if (!exists) {
            throw new RuntimeException("해당 상점이 존재하지 않습니다.");
        }
        StoreEntity storeEntity = this.storeRepository.save(new StoreEntity(store));
        return Store.fromEntity(storeEntity);
    }

    /**
     * 매장을 삭제하는 메서드
     *
     * @param store (삭제할 매장 정보)
     */
    public void deleteStore(Store store) {
        boolean exists = this.storeRepository.existsById(store.getId());
        if (!exists) {
            throw new RuntimeException("해당 상점이 존재하지 않습니다.");
        }
        this.storeRepository.deleteById(store.getId());
    }
}
