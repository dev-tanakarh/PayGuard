package com.tanakarh.payguard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanakarh.payguard.domain.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByEmail(String email);
}
