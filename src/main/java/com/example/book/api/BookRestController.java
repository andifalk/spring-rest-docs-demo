package com.example.book.api;

import com.example.book.api.resource.BookListResource;
import com.example.book.api.resource.BookResource;
import com.example.book.api.resource.CreateBookResource;
import com.example.book.api.resource.UpdateBookResource;
import com.example.book.boundary.BookService;
import com.example.book.entity.Book;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

/**
 * RESTful api for {@link BookResource book resources}.
 */
@RestController
@RequestMapping(path = BookRestController.BOOK_RESOURCE_PATH,
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"})
public class BookRestController {

    public static final String BOOK_RESOURCE_PATH = "/books";

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createBook(@Valid @RequestBody CreateBookResource createBookResource) {
        Book book = bookService.createBook(createBookResource.createBook());

        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{bookId}")
                        .buildAndExpand(book.getIdentifier()).toUri())
                .eTag(book.getVersion().toString())
                .body(new BookResource(book));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByIdentifier(@PathVariable("id") UUID identifier) {
        Book book = bookService.findByIdentifier(identifier);
        if (book == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().eTag(book.getVersion().toString()).body(new BookResource(book));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAllBooks() {
        return new ResponseEntity<>(new BookListResource(bookService.findAllBooks(), null, null), HttpStatus.OK);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> findByQuery(
            @RequestParam(required = false, name = "isbn") String isbn,
            @RequestParam(required = false, name = "title") String title) {
        if (StringUtils.isNotBlank(isbn)) {
            Collection<Book> books = bookService.findByIsbn(isbn);
            if (books.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return new ResponseEntity<>(new BookListResource(books, isbn, title), HttpStatus.OK);
            }
        } else if (StringUtils.isNotBlank(title)) {
            Collection<Book> books = bookService.findByTitle(title);
            if (books.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return new ResponseEntity<>(new BookListResource(books, isbn, title), HttpStatus.OK);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBook(
            WebRequest request,
            @PathVariable("id") UUID identifier,
            @Valid @RequestBody UpdateBookResource updateBookResource) {
        Book book = bookService.findByIdentifier(identifier);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.checkNotModified(book.getVersion().toString())) {
            book.setDescription(updateBookResource.getDescription());
            book.setGenre(updateBookResource.getGenre());
            book.setIsbn(updateBookResource.getIsbn());
            book.setTitle(updateBookResource.getTitle());
            book = bookService.updateBook(book);
            return new ResponseEntity<>(new BookResource(book), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable("id") UUID identifier) {
        if (bookService.deleteBook(identifier)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
