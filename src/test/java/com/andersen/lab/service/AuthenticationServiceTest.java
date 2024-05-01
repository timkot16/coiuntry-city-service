package com.andersen.lab.service;

import com.andersen.lab.config.security.JwtService;
import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.entity.enums.Role;
import com.andersen.lab.exception.RegistrationException;
import com.andersen.lab.mapper.UserMapper;
import com.andersen.lab.model.AuthenticationRequest;
import com.andersen.lab.model.AuthenticationResponse;
import com.andersen.lab.model.RegisterRequest;
import com.andersen.lab.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    JwtService jwtService;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserMapper userMapper;
    @Mock
    Authentication authentication;

    @Test
    void whenAuthenticate_thenSuccess() {
        AuthenticationRequest request = AuthenticationRequest.builder().email("test@mail.com").password("pass").build();
        UserEntity userEntity = UserEntity.builder().firstName("Test").lastName("Testov").email("test@mail.com").password("pass").role(Role.USER).build();
        String token = "expected.access.token";
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(userEntity));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())))
                .thenReturn(authentication);
        when(jwtService.generateToken(any())).thenReturn(token);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals(token, response.token());

        verify(userRepository, times(1)).findByEmail(request.email());
    }

    @Test
    void whenAuthenticate_thenUsernameNotFoundExceptionThrown() {
        AuthenticationRequest request = AuthenticationRequest.builder().email("test@mail.com").password("pass").build();
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())))
                .thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void whenRegister_thenSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Test")
                .lastName("Testov")
                .email("test@mail.com")
                .password("pass")
                .build();
        String token = "expected.access.token";
        UserEntity userEntity = UserEntity.builder().firstName("Test").lastName("Testov").email("test@mail.com").password("pass").role(Role.USER).build();
        UserEntity savedUserEntity = UserEntity.builder().id(1).firstName("Test").lastName("Testov").email("test@mail.com").password("pass").role(Role.USER).build();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(jwtService.generateToken(any())).thenReturn(token);

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals(token, response.token());

        verify(userRepository, times(1)).findByEmail(request.email());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void whenRegister_thenRegistrationExceptionThrown() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Test")
                .lastName("Testov")
                .email("test@mail.com")
                .password("pass")
                .build();

        UserEntity userEntity = UserEntity.builder().id(1).firstName("Test").lastName("Testov").email("test@mail.com").password("pass").role(Role.USER).build();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(userEntity));

        assertThrows(RegistrationException.class, () -> authenticationService.register(request));
    }
}