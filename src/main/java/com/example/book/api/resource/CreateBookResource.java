package com.example.book.api.resource;

import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Resource to create a new {@link BookResource}.
 */
public class CreateBookResource extends UpdateBookResource {

    private UUID identifier;

    /**
     * Constructor for jackson.
     */
    public CreateBookResource() {
        super();
    }

    /**
     * Constructor.
     *
     * @param title book title
     * @param isbn book isbn number
     * @param description book description
     * @param genre book genre
     */
    public CreateBookResource(String title, String isbn, String description, Genre genre) {
        this(null, title, isbn, description, genre);
    }

    /**
     * Constructor.
     *
     * @param identifier book identifier
     * @param title book title
     * @param isbn book isbn number
     * @param description book description
     * @param genre book genre
     */
    public CreateBookResource(
            @JsonProperty("id") UUID identifier, @JsonProperty("title") String title,
            @JsonProperty("isbn") String isbn, @JsonProperty("description") String description,
            @JsonProperty("genre") Genre genre) {
        super(title, isbn, description, genre);
        this.identifier = identifier;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public CreateBookResource setIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    public Book createBook() {
        return new Book(identifier, getTitle(), getIsbn(), getDescription(), getGenre());
    }
}
