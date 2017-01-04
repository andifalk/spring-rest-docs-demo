package com.example.user.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * User entity.
 */
@Entity
public class User extends AbstractPersistable<Long> implements UserDetails {

    private static final int MAX_USER_IDENTIFIER_LENGTH = 50;
    private static final int MAX_USER_FIRSTNAME_LENGTH = 50;
    private static final int MAX_USER_LASTNAME_LENGTH = 50;
    private static final int MAX_USER_USERNAME_LENGTH = 30;
    private static final int MAX_USER_PASSWORD_LENGTH = 100;

    @NotNull
    @Column(nullable = false, length = MAX_USER_IDENTIFIER_LENGTH)
    private UUID identifier;

    @NotNull
    @Size(min = 1, max = MAX_USER_FIRSTNAME_LENGTH)
    @Column(nullable = false, length = MAX_USER_FIRSTNAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(min = 1, max = MAX_USER_LASTNAME_LENGTH)
    @Column(nullable = false, length = MAX_USER_LASTNAME_LENGTH)
    private String lastName;

    @NotNull
    @Size(min = 4, max = MAX_USER_USERNAME_LENGTH)
    @Column(nullable = false, length = MAX_USER_USERNAME_LENGTH)
    private String username;

    @NotNull
    @Size(min = 5, max = MAX_USER_PASSWORD_LENGTH)
    @Column(nullable = false, length = MAX_USER_PASSWORD_LENGTH)
    private String password;

    public User() {
    }

    public User(String firstName, String lastName, String username, String password) {
        this(null, firstName, lastName, username, password);
    }

    public User(UUID identifier, String firstName, String lastName, String username, String password) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
