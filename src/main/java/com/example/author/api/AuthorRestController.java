package com.example.author.api;

import com.example.author.api.resource.AuthorListResource;
import com.example.author.api.resource.AuthorResource;
import com.example.author.api.resource.CreateAuthorResource;
import com.example.author.boundary.AuthorService;
import com.example.author.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

/**
 * RESTful api for {@link AuthorResource author resources}.
 */
@RestController
@RequestMapping(path = AuthorRestController.AUTHOR_RESOURCE_PATH)
public class AuthorRestController {

    public static final String AUTHOR_RESOURCE_PATH = "/authors";

    private final AuthorService authorService;

    /**
     * Constructor for {@link AuthorRestController}.
     *
     * @param authorService the {@link AuthorService}
     */
    @Autowired
    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createAuthor(@Valid @RequestBody CreateAuthorResource createAuthorResource) {
        Author author = authorService.createAuthor(createAuthorResource.createAuthor());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{authorId}")
                .buildAndExpand(author.getIdentifier()).toUri());
        return new ResponseEntity<>(new AuthorResource(author), httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByIdentifier(@PathVariable("id") UUID identifier) {
        Author author = authorService.findByIdentifier(identifier);
        if (author == null) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(new AuthorResource(author), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAllAuthors() {
        return new ResponseEntity<>(new AuthorListResource(authorService.findAllAuthors(), null), HttpStatus.OK);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> findByQuery(
            @RequestParam(name = "lastname") String lastname) {
        Collection<Author> authors = authorService.findByLastname(lastname);
        if (authors.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(new AuthorListResource(authors, lastname), HttpStatus.OK);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAuthor(@PathVariable("id") UUID identifier) {
        if (authorService.deleteAuthor(identifier)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
