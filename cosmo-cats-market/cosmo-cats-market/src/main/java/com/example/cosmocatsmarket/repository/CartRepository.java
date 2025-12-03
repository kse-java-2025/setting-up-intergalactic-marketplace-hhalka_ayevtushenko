package com.example.cosmocatsmarket.repository;

import com.example.cosmocatsmarket.repository.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
}