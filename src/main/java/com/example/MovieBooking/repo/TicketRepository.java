package com.example.MovieBooking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MovieBooking.model.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
