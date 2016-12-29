package com.example.book.api.resource;

import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.example.common.StringEnumeration;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Resource to create a new {@link BookResource}.
 */
public class UpdateBookResource {

    @NotNull
    @Size(min = Book.MIN_BOOK_TITLE_LENGTH, max = Book.MAX_BOOK_TITLE_LENGTH)
    private String title;

    @NotNull
    @Pattern(regexp = "978-[0-9-]{13}")
    @Size(min = Book.MIN_BOOK_ISBN_LENGTH, max = Book.MAX_BOOK_ISBN_LENGTH)
    private String isbn;

    @Size(max = Book.MAX_BOOK_DESCRIPTION_LENGTH)
    private String description;

    @NotNull
    @StringEnumeration(
            enumClass = Genre.class,
            enumValues = "FANTASY, HORROR, HUMOR, SCIENCE_FICTION, MYSTERY, WESTERN, CRIME, BIOGRAPHY, COMPUTER")
    private Genre genre;

    /**
     * Constructor for jackson.
     */
    public UpdateBookResource() {
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
    public UpdateBookResource(
            @JsonProperty("title") String title,
            @JsonProperty("isbn") String isbn, @JsonProperty("description") String description,
            @JsonProperty("genre") Genre genre) {
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public UpdateBookResource setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public UpdateBookResource setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UpdateBookResource setDescription(String description) {
        this.description = description;
        return this;
    }

    public Genre getGenre() {
        return genre;
    }

    public UpdateBookResource setGenre(Genre genre) {
        this.genre = genre;
        return this;
    }

}
