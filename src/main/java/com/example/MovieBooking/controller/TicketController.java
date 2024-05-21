package com.example.MovieBooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MovieBooking.resource.BookingResource;
import com.example.MovieBooking.resource.TicketResource;
import com.example.MovieBooking.service.TicketService;

import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping("ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@PostMapping("book")
	public ResponseEntity<TicketResource> bookTicket(@RequestBody BookingResource bookingResource) {

		log.info("Received Request to book ticket: " + bookingResource);

		return ResponseEntity.ok(ticketService.bookTicket(bookingResource));
	}

	@GetMapping("{id}")
	public ResponseEntity<TicketResource> getTicket(@PathVariable(name = "id") @Min(value = 1, message = "Ticket Id Cannot be -ve") long id) {

		log.info("Received Request to get ticket: " + id);

		return ResponseEntity.ok(ticketService.getTicket(id));
	}
}
