package info.andifalk.book.api.resource;

import info.andifalk.book.api.BookRestController;
import info.andifalk.book.entity.Book;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Resource for a collection of {@link BookResource book resources}.
 */
public class BookListResource extends ResourceSupport {

    private final Collection<BookResource> bookResources;

    public BookListResource(Collection<Book> books, String isbn, String title) {
        Assert.notNull(books, "Books must be set");

        this.bookResources = books.stream().map(BookResource::new).collect(Collectors.toList());

        if (StringUtils.isNotBlank(isbn) || StringUtils.isNotBlank(title)) {
            add(linkTo(methodOn(BookRestController.class)
                    .findByQuery(isbn, title)).withSelfRel());
        } else {
            add(linkTo(methodOn(BookRestController.class)
                    .findAllBooks()).withSelfRel());
        }
    }

    public Collection<BookResource> getBooks() {
        return bookResources;
    }
}
