package com.tanakarh.payguard.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanakarh.payguard.entity.user.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID>{

}
