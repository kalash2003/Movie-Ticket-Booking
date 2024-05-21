package com.example.MovieBooking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MovieBooking.model.TheaterSeat;

@Repository
public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

}
