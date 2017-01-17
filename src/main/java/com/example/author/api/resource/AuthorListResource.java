package com.example.author.api.resource;

import com.example.author.api.AuthorRestController;
import com.example.author.entity.Author;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for a collection of {@link AuthorResource author resources}.
 */
@XmlRootElement(name = "Authors")
public class AuthorListResource extends ResourceSupport {

    public AuthorListResource() {
        super();
    }

    private Collection<AuthorResource> authorResources;

    public AuthorListResource(Collection<Author> authors, String lastName) {
        Assert.notNull(authors, "Authors must be set");

        this.authorResources = authors.stream().map(AuthorResource::new).collect(Collectors.toList());

        if (StringUtils.isNotBlank(lastName)) {
            add(linkTo(methodOn(AuthorRestController.class)
                    .findByQuery(lastName)).withSelfRel());
        } else {
            add(linkTo(methodOn(AuthorRestController.class)
                    .findAllAuthors()).withSelfRel());
        }
    }

    @XmlElement(name = "Author")
    public Collection<AuthorResource> getAuthors() {
        return authorResources;
    }
}
