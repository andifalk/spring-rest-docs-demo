package com.example.user.boundary;

import com.example.user.entity.User;

import java.util.UUID;

/**
 * Service to manage {@link User users}.
 */
public interface UserService {

    /**
     * Creates a new {@link User user}.
     *
     * @param user user to create
     * @return created persistent user
     */
    User createUser(User user);

    /**
     * Finds unique user for given identifier.
     *
     * @param identifier identifier for user to find
     * @return user or null if none found
     */
    User findByIdentifier(UUID identifier);

    /**
     * Finds unique user for given username.
     *
     * @param username user name for user to find
     * @return user or null if none found
     */
    User findByUsername(String username);

}
