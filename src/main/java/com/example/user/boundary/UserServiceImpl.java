package com.example.user.boundary;

import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.UUID;

/**
 * Implementation for {@link UserService}.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }

    @Override
    public User createUser(User user) {
        if (user.getIdentifier() == null) {
            user.setIdentifier(idGenerator.generateId());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByIdentifier(UUID identifier) {
        return userRepository.findOneByIdentifier(identifier);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }
}
