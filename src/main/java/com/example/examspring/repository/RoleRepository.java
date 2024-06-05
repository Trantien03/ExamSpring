package com.example.examspring.repository;

import com.example.examspring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUserId(String userId);
}
