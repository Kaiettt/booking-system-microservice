package com.booking.userservice.mapper;

import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.request.UserUpdateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.enumerate.Gender;
import com.booking.userservice.enumerate.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "role", source = "role")
    User toEntity(UserCreateRequest request);

    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromUpdateDto(UserUpdateRequest request, @MappingTarget User user);

    // MapStruct will automatically find and use these methods
    default Gender mapGender(String gender) {
        return gender != null ? Gender.valueOf(gender.toUpperCase()) : null;
    }

    default Role mapRole(String role) {
        return role != null ? Role.valueOf(role.toUpperCase()) : null;
    }
}
