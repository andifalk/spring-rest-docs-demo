package info.andifalk.author.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.andifalk.author.api.AuthorRestController;
import info.andifalk.author.entity.Author;
import info.andifalk.author.entity.Gender;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for a {@link AuthorResource author}.
 */
public class AuthorResource extends ResourceSupport {

    private final Author author;

    public AuthorResource(Author author) {
        this.author = author;

        add(linkTo(methodOn(AuthorRestController.class)
                .findByIdentifier(author.getIdentifier())).withSelfRel());
    }

    @JsonProperty("id")
    public UUID getIdentifier() {
        return author.getIdentifier();
    }

    public Gender getGender() {
        return author.getGender();
    }

    public String getFirstname() {
        return author.getFirstname();
    }

    public String getLastname() {
        return author.getLastname();
    }
}
