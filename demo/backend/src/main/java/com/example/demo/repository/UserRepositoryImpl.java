package com.example.demo.repository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

import com.example.demo.model.User;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    public UserRepositoryImpl() {
        users.add(new User(1L, "John"));
        users.add(new User(2L, "Jane"));
    }


    @Override
    public User save(User entity) {
        users.add(entity);
        return entity;
    }

    @Override
    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        logger.info("Users:"+ users);
        return users;
    }
    
}
