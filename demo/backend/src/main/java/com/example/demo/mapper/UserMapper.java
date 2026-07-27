package com.example.demo.mapper;

import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;

@Component
public class UserMapper {
    
    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());

        return user;
    }

    public UserResponse toResponse(User user) {
        if(user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());

        return response;
    }

    public void updateEntityFromDto(UpdateUserRequest request, User existingUser) {
        if(request == null || existingUser == null) {
            return;
        }

        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());
        existingUser.setFirstname(request.getFirstname());
        existingUser.setLastname(request.getLastname());
    }
}
