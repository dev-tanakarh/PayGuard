package com.tanakarh.payguard.domain.entity.user.merchant;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "merchants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false, unique = true)
    private String businessName;

    @Column(name = "business_email", nullable = false, unique = true)
    private String businessEmail;

    private String phone;

    @Column(name = "business_address", nullable = false, unique = true)
    private String businessAddress;

    @Column(name = "reg_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

     @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        status = MerchantStatus.PENDING;
    }

    @PreUpdate
     void onUpdate(){
        updatedAt = Instant.now();
    }
}
