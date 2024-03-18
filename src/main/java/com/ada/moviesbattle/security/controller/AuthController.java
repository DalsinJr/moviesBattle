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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "Controller for user register a new user and login into the system")
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Login into the system with username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        var token = tokenService.generateToken((UserEntity) authenticate.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "409", description = "User already exists"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseEntity.ok().build();
    }


}
