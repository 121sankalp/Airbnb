package com.sankalp.projects.airBnbApp.repository;

import com.sankalp.projects.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  GuestRepository extends JpaRepository<Guest, Long> {
}
