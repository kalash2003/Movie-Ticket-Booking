package com.example.MovieBooking.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieBooking.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	Double getReviewAverage(long id);

}
