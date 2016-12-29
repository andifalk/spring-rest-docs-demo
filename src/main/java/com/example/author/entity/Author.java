package com.example.author.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Author entity.
 */
@Entity
public class Author extends AbstractPersistable<Long> {

    public static final int MAX_AUTHOR_FIRSTNAME_LENGTH = 50;
    public static final int MIN_AUTHOR_FIRSTNAME_LENGTH = 1;
    public static final int MAX_AUTHOR_LASTNAME_LENGTH = 50;
    public static final int MIN_AUTHOR_LASTNAME_LENGTH = 1;
    private static final int MAX_AUTHOR_IDENTIFIER_LENGTH = 50;
    private static final int MAX_AUTHOR_GENDER_LENGTH = 30;

    @SuppressWarnings("unused")
    @Version
    @NotNull
    @Column(nullable = false)
    private Long version;

    @NotNull
    @Column(nullable = false, length = MAX_AUTHOR_IDENTIFIER_LENGTH)
    private UUID identifier;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_AUTHOR_GENDER_LENGTH)
    private Gender gender;

    @NotNull
    @Size(min = MIN_AUTHOR_FIRSTNAME_LENGTH, max = MAX_AUTHOR_FIRSTNAME_LENGTH)
    @Column(nullable = false, length = MAX_AUTHOR_FIRSTNAME_LENGTH)
    private String firstname;

    @NotNull
    @Size(min = MIN_AUTHOR_LASTNAME_LENGTH, max = MAX_AUTHOR_LASTNAME_LENGTH)
    @Column(nullable = false, length = MAX_AUTHOR_LASTNAME_LENGTH)
    private String lastname;

    /**
     * Constructor for JPA.
     */
    @SuppressWarnings("unused")
    public Author() {
        super();
    }

    /**
     * Constructor.
     *
     * @param gender author's gender
     * @param firstname author's first name
     * @param lastname author's last name
     */
    public Author(Gender gender, String firstname, String lastname) {
        this(null, gender, firstname, lastname);
    }

    /**
     * Constructor.
     *
     * @param identifier identifier for author
     * @param gender author's gender
     * @param firstname author's first name
     * @param lastname author's last name
     */
    public Author(UUID identifier, Gender gender, String firstname, String lastname) {
        this.identifier = identifier;
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Author setIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("identifier", identifier)
                .append("gender", gender)
                .append("firstname", firstname)
                .append("lastname", lastname)
                .toString();
    }
}
