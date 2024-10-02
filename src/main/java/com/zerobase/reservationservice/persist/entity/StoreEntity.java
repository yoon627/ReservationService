package com.zerobase.reservationservice.persist.entity;

import com.zerobase.reservationservice.model.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "STORE")
@Getter
@NoArgsConstructor
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String address;

    private String description;

    private String sellerName;

    public StoreEntity(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.description = store.getDescription();
        this.sellerName = store.getSellerName();
    }
}
