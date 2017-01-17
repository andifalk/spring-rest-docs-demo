package com.example.book.api.resource;

import com.example.author.api.resource.AuthorResource;
import com.example.book.api.BookRestController;
import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for {@link Book books}.
 */
@XmlRootElement
public class BookResource extends ResourceSupport {

    private Book book;

    public BookResource() {
        super();
    }

    public BookResource(Book book) {
        this.book = book;

        add(ControllerLinkBuilder.linkTo(methodOn(BookRestController.class)
                .findByIdentifier(book.getIdentifier())).withSelfRel());
    }

    @XmlElement(name="Title")
    public String getTitle() {
        return book.getTitle();
    }

    @XmlElement(name="Isbn")
    public String getIsbn() {
        return book.getIsbn();
    }

    @XmlElement(name="Description")
    public String getDescription() {
        return book.getDescription();
    }

    @XmlElement(name="Genre")
    public Genre getGenre() {
        return book.getGenre();
    }

    @XmlElement(name="Id")
    @JsonProperty("id")
    public UUID getIdentifier() {
        return book.getIdentifier();
    }

    @XmlElement(name="Author")
    public Set<AuthorResource> getAuthors() {
        return book.getAuthors().stream().map(AuthorResource::new).collect(Collectors.toSet());
    }
}
