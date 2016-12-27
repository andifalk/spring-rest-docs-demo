package com.example.book.api.resource;

import com.example.author.entity.Author;
import com.example.book.api.BookRestController;
import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for {@link Book books}.
 */
public class BookResource extends ResourceSupport {

    private final Book book;

    public BookResource(Book book) {
        this.book = book;

        add(ControllerLinkBuilder.linkTo(methodOn(BookRestController.class)
                .findByIdentifier(book.getIdentifier())).withSelfRel());
    }

    public String getTitle() {
        return book.getTitle();
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    public String getDescription() {
        return book.getDescription();
    }

    public Genre getGenre() {
        return book.getGenre();
    }

    @JsonProperty("id")
    public UUID getIdentifier() {
        return book.getIdentifier();
    }

    public Set<String> getAuthors() {
        return book.getAuthors().stream().map(Author::getFullname).collect(Collectors.toSet());
    }
}
