package com.example.MovieBooking.resource;

import lombok.*;

import java.util.Date;

import com.example.MovieBooking.enums.SeatType;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowSeatResource {

	private long id;

	private String seatNumber;

	private int rate;

	private SeatType seatType;

	private boolean booked;

	private Date bookedAt;

}
