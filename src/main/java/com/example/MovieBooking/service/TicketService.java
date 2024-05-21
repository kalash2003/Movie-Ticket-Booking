package com.example.MovieBooking.service;

import com.example.MovieBooking.exception.NotFoundException;
import com.example.MovieBooking.model.MyUser;
import com.example.MovieBooking.model.Show;
import com.example.MovieBooking.model.ShowSeat;
import com.example.MovieBooking.model.Ticket;
import com.example.MovieBooking.repo.ShowRepository;
import com.example.MovieBooking.repo.TicketRepository;
import com.example.MovieBooking.repo.UserRepository;
import com.example.MovieBooking.resource.BookingResource;
import com.example.MovieBooking.resource.TicketMessage;
import com.example.MovieBooking.resource.TicketResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TicketService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	KafkaTemplate<String,String> kafkaTemplate;

	private static String topic="TICKET_BOOKED";

	ObjectMapper mapper=new ObjectMapper();

	public TicketResource bookTicket(BookingResource bookingResource) {

		Optional<MyUser> optionalUser = userRepository.findById(bookingResource.getUserId());

		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User Not Found with ID: " + bookingResource.getUserId() + " to book ticket");
		}

		Optional<Show> optionalShow = showRepository.findById(bookingResource.getShowId());

		if (optionalShow.isEmpty()) {
			throw new NotFoundException("Show Not Found with ID: " + bookingResource.getUserId() + " to book ticket");
		}

		Set<String> requestedSeats = bookingResource.getSeatsNumbers();

		List<ShowSeat> showSeatsEntities = optionalShow.get().getSeats();

		showSeatsEntities =
				showSeatsEntities
						.stream()
						.filter(seat -> seat.getSeatType().equals(bookingResource.getSeatType())
								&& !seat.isBooked()
								&& requestedSeats.contains(seat.getSeatNumber()))
						.collect(Collectors.toList());

		if (showSeatsEntities.size() != requestedSeats.size()) {
			throw new NotFoundException("Seats Not Available for Booking");
		}

		Ticket ticket =
				Ticket.builder()
						.myUser(optionalUser.get())
						.show(optionalShow.get())
						.seats(showSeatsEntities)
						.build();

		double amount = 0.0;
		String allotedSeats = "";

		for (ShowSeat seatsEntity : showSeatsEntities) {
			seatsEntity.setBooked(true);
			seatsEntity.setBookedAt(new Date());
			seatsEntity.setTicket(ticket);

			amount += seatsEntity.getRate();

			allotedSeats += seatsEntity.getSeatNumber() + " ";
		}

		ticket.setAmount(amount);
		ticket.setAllottedSeats(allotedSeats);

		if (CollectionUtils.isEmpty(optionalUser.get().getTicketEntities())) {
			optionalUser.get().setTicketEntities(new ArrayList<>());
		}

		optionalUser.get().getTicketEntities().add(ticket);

		if (CollectionUtils.isEmpty(optionalShow.get().getTickets())) {
			optionalShow.get().setTickets(new ArrayList<>());
		}

		optionalShow.get().getTickets().add(ticket);

		ticket = ticketRepository.save(ticket);

		try {
			TicketMessage message = new TicketMessage(ticket.getMyUser().getName(),ticket.getMyUser().getMobile(),ticket.getMyUser().getEmail(), ticket.getShow(), ticket.getSeats());
			log.info("sending kafka message on booking {}", mapper.writeValueAsString(message));
			kafkaTemplate.send(topic, mapper.writeValueAsString(message));
		}catch (Exception e){
				log.error("Exception while sending notification service");
		}

		return Ticket.toResource(ticket);
	}


	public TicketResource getTicket(long id) {
		Optional<Ticket> ticketEntity = ticketRepository.findById(id);

		if (ticketEntity.isEmpty()) {
			log.error("Ticket not found for id: " + id);
			throw new EntityNotFoundException("Ticket Not Found with ID: " + id);
		}

		return Ticket.toResource(ticketEntity.get());
	}

}
