package com.example.MovieBooking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MovieBooking.model.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long>{

	boolean existsByMobile(String mobile);

	MyUser findByName(String name);
}
