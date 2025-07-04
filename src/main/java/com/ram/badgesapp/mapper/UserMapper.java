package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.UserRequest;
import com.ram.badgesapp.dto.UserResponse;
import com.ram.badgesapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest userRequest);

    @Mapping(target = "employee", source = "employeeId")
    User toEntity(UserResponse userResponse);

    @Mapping(target = "employeeId", source = "employee.id")
    UserResponse toUserResponse(User user);

    UserRequest toUserRequest(User user);

}
