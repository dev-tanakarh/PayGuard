package com.tanakarh.payguard.domain.entity.user.merchant;

import java.time.Instant;

import com.tanakarh.payguard.domain.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "business_name", nullable = false, unique = true)
    private String businessName;

    private String phone;

    @Column(name = "business_address", nullable = false, unique = true)
    private String businessAddress;

    @Column(name = "reg_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "created_at")
    private Instant createdAt;

     @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    @PreUpdate
     void onUpdate(){
        updatedAt = Instant.now();
    }
}
