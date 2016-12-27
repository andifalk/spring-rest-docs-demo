package info.andifalk.book.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.andifalk.author.entity.Author;
import info.andifalk.book.api.BookRestController;
import info.andifalk.book.entity.Book;
import info.andifalk.book.entity.Genre;
import org.springframework.hateoas.ResourceSupport;

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

        add(linkTo(methodOn(BookRestController.class)
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
