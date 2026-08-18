package com.tanakarh.payguard.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanakarh.payguard.domain.entity.user.admin.Admin;


public interface AdminRepository extends JpaRepository<Admin, UUID>{

}
