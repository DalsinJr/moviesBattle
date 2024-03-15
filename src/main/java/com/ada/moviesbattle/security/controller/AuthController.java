package com.ada.moviesbattle.security.controller;

import com.ada.moviesbattle.security.domain.dto.AuthDTO;
import com.ada.moviesbattle.security.domain.dto.LoginResponseDTO;
import com.ada.moviesbattle.security.domain.dto.UserDTO;
import com.ada.moviesbattle.security.domain.entity.UserEntity;
import com.ada.moviesbattle.security.service.TokenService;
import com.ada.moviesbattle.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "Controller for user authentication")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;


    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO authDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        var authenticate = authenticationManager.authenticate(authenticationToken);
        var token = tokenService.generateToken((UserEntity) authenticate.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseEntity.ok().build();
    }


}
