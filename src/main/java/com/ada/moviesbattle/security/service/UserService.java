package com.ada.moviesbattle.security.service;

import com.ada.moviesbattle.security.domain.dto.UserDTO;
import com.ada.moviesbattle.security.domain.entity.UserEntity;
import com.ada.moviesbattle.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()) != null)
            throw new IllegalArgumentException("User already exists");
        userRepository.save(UserEntity.fromDTO(userDTO));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails byUsername = userRepository.findByUsername(username);
        if (byUsername == null) throw new UsernameNotFoundException("User not found");
        return byUsername;
    }
}
