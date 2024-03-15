package com.ada.moviesbattle.security.repository;


import com.ada.moviesbattle.security.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserDetails findByUsername(String username);
}
