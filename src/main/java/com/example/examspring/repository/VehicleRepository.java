package com.example.examspring.repository;

import com.example.examspring.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
