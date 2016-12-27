package info.andifalk.author.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.andifalk.author.entity.Author;
import info.andifalk.author.entity.Gender;
import info.andifalk.book.entity.Book;
import info.andifalk.book.entity.Genre;
import info.andifalk.common.StringEnumeration;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

import static info.andifalk.author.entity.Author.*;

/**
 * Resource to create a new {@link AuthorResource}.
 */
public class CreateAuthorResource {

    @NotNull
    private UUID identifier;

    @NotNull
    @StringEnumeration(enumClass = Gender.class, enumValues = "MALE, FEMALE")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Size(min = MIN_AUTHOR_FIRSTNAME_LENGTH, max = MAX_AUTHOR_FIRSTNAME_LENGTH)
    private String firstname;

    @NotNull
    @Size(min = MIN_AUTHOR_LASTNAME_LENGTH, max = MAX_AUTHOR_LASTNAME_LENGTH)
    private String lastname;


    /**
     * Constructor for jackson.
     */
    public CreateAuthorResource() {
        super();
    }

    /**
     * Constructor.
     *
     * @param firstname author's firstname
     * @param lastname author's lastname
     * @param gender author's gender
     */
    public CreateAuthorResource(String firstname, String lastname, Gender gender) {
        this(null, firstname, lastname, gender);
    }

    /**
     * Constructor.
     *
     * @param identifier author's identifier
     * @param firstname author's firstname
     * @param lastname author's lastname
     * @param gender author's gender
     */
    public CreateAuthorResource(
            @JsonProperty("id") UUID identifier, @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("gender") Gender gender) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public CreateAuthorResource setIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
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

    public Author createAuthor() {
        return new Author(identifier, gender, firstname, lastname);
    }
}
