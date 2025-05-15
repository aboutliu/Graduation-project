package org.example.springbootdemo.virtual.repository;

import org.example.springbootdemo.virtual.model.Virtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualRepository extends JpaRepository<Virtual, Long> {
}