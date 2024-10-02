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

    public Store searchStore(String name) {
        StoreEntity store = this.storeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("해당 상점이 존재하지 않습니다."));
        return Store.fromEntity(store);
    }

    public Store addStore(Store store) {
        //TODO id로만 체크하는게 맞는건지 확인하기
        boolean exists = this.storeRepository.existsById(store.getId());
        if (exists) {
            throw new RuntimeException("이미 존재하는 상점입니다.");
        }
        //TODO store id가 null일때 정상 작동하는지 확인하기
        StoreEntity storeEntity = this.storeRepository.save(new StoreEntity(store));
        return Store.fromEntity(storeEntity);
    }

    public Store updateStore(Store store) {
        boolean exists = this.storeRepository.existsById(store.getId());
        if (!exists) {
            throw new RuntimeException("해당 상점이 존재하지 않습니다.");
        }
        StoreEntity storeEntity = this.storeRepository.save(new StoreEntity(store));
        return Store.fromEntity(storeEntity);
    }

    public void deleteStore(Store store) {
        boolean exists = this.storeRepository.existsById(store.getId());
        if (!exists) {
            throw new RuntimeException("해당 상점이 존재하지 않습니다.");
        }
        this.storeRepository.deleteById(store.getId());
    }
}
