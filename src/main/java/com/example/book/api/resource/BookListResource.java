package com.example.book.api.resource;

import com.example.book.api.BookRestController;
import com.example.book.entity.Book;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for a collection of {@link BookResource book resources}.
 */
@XmlRootElement(name = "Books")
public class BookListResource extends ResourceSupport {

    private Collection<BookResource> bookResources;

    public BookListResource() {
        super();
    }

    public BookListResource(Collection<Book> books, String isbn, String title) {
        Assert.notNull(books, "Books must be set");

        this.bookResources = books.stream().map(BookResource::new).collect(Collectors.toList());

        if (StringUtils.isNotBlank(isbn) || StringUtils.isNotBlank(title)) {
            add(ControllerLinkBuilder.linkTo(methodOn(BookRestController.class)
                    .findByQuery(isbn, title)).withSelfRel());
        } else {
            add(linkTo(methodOn(BookRestController.class)
                    .findAllBooks()).withSelfRel());
        }
    }

    @XmlElement(name="Book")
    public Collection<BookResource> getBooks() {
        return bookResources;
    }
}
