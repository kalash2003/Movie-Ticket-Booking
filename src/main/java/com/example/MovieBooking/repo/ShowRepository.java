package com.example.MovieBooking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.MovieBooking.model.Show;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query(value = "select * from shows s, movies m , theaters t where m.id=s.movie_id and s.theater_id=t.id and m.title=? and city=?",nativeQuery = true)
    List<Show> findByMovieNameAndCity(String movieName,String city);

    @Query(value= "select * from shows s, theaters t where s.theater_id=t.id and t.city=?",nativeQuery = true)
    List<Show> findByCity(String city);

    @Query(value =" select * from shows s, theaters t where s.theater_id=t.id and t.name=? and t.city=?",nativeQuery = true)
    List<Show> findByTheaterAndCity(String theaterName, String cityName);
}
