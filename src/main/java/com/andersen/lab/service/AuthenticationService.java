package com.andersen.lab.service;

import com.andersen.lab.config.security.JwtService;
import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.exception.RegistrationException;
import com.andersen.lab.mapper.UserMapper;
import com.andersen.lab.model.AuthenticationRequest;
import com.andersen.lab.model.AuthenticationResponse;
import com.andersen.lab.model.RegisterRequest;
import com.andersen.lab.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    JwtService jwtService;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    UserMapper userMapper;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserEntity userEntity = userRepository.findByEmail(request.email())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    log.warn("User with email [{}] already exist.", request.email());
                    throw new RegistrationException("Registration failed. Please check your data and try again.");
                });
        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity newUserEntity = userRepository.save(userEntity);
        String jwtToken = jwtService.generateToken(newUserEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
