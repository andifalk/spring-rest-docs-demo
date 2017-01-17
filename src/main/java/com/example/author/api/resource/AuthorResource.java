package com.example.author.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.author.api.AuthorRestController;
import com.example.author.entity.Author;
import com.example.author.entity.Gender;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for a {@link AuthorResource author}.
 */
@XmlRootElement
public class AuthorResource extends ResourceSupport {

    private Author author;

    public AuthorResource() {
        super();
    }

    public AuthorResource(Author author) {
        this.author = author;

        add(linkTo(methodOn(AuthorRestController.class)
                .findByIdentifier(author.getIdentifier())).withSelfRel());
    }

    @XmlElement(name = "Id")
    @JsonProperty("id")
    public UUID getIdentifier() {
        return author.getIdentifier();
    }

    @XmlElement(name = "Gender")
    public Gender getGender() {
        return author.getGender();
    }

    @XmlElement(name = "Firstname")
    public String getFirstname() {
        return author.getFirstname();
    }

    @XmlElement(name = "Lastname")
    public String getLastname() {
        return author.getLastname();
    }
}
