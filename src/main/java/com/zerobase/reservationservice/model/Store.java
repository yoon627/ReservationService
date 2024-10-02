package com.zerobase.reservationservice.model;

import com.zerobase.reservationservice.persist.entity.StoreEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {

    private Long id;
    private String name;
    private String address;
    private String description;
    private String sellerName;

    public static Store fromEntity(StoreEntity storeEntity) {
        return Store.builder()
                .id(storeEntity.getId())
                .name(storeEntity.getName())
                .address(storeEntity.getAddress())
                .description(storeEntity.getDescription())
                .sellerName(storeEntity.getSellerName())
                .build();
    }
}
