package com.andersen.lab.mapper;

import com.andersen.lab.entity.UserEntity;
import com.andersen.lab.model.RegisterRequest;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "password", source = "password", qualifiedByName = "getEncodedPassword")
    @Mapping(target = "role", expression = "java(com.andersen.lab.entity.enums.Role.USER)")
    public abstract UserEntity toEntity(RegisterRequest registerRequest);

    @Named("getEncodedPassword")
    String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
