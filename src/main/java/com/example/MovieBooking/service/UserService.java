package com.example.MovieBooking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBooking.model.MyUser;
import com.example.MovieBooking.repo.UserRepository;
import com.example.MovieBooking.resource.UserResource;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserResource addUser(UserResource userResource) {

        if (userRepository.existsByMobile(userResource.getMobile())) {
            return userResource;
        }

		MyUser user = MyUser.toEntity(userResource);

        user = userRepository.save(user);

        log.info("Added New User"+user.toString());

        return MyUser.toResource(user);
    }


    public UserResource getUser(long id) {
        Optional<MyUser> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            log.error("User not found for id: " + id);
            throw new EntityNotFoundException("User Not Found with ID: " + id);

        }

        return MyUser.toResource(userEntity.get());
    }

}
