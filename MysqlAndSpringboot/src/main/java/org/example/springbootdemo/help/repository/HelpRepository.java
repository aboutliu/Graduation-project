package org.example.springbootdemo.help.repository;

import org.example.springbootdemo.help.model.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
}