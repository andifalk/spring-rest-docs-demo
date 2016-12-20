package info.andifalk.book.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.andifalk.book.entity.Book;
import info.andifalk.book.entity.Genre;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

import static info.andifalk.book.entity.Book.*;

/**
 * Resource to create a new {@link BookResource}.
 */
public class CreateBookResource {

    @NotNull
    private UUID identifier;

    @NotNull
    @Size(min = MIN_BOOK_TITLE_LENGTH, max = MAX_BOOK_TITLE_LENGTH)
    private String title;

    @NotNull
    @Pattern(regexp = "^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-]){4})[-0-9]{17}$)" +
            "97[89][-]?[0-9]{1,5}[-]?[0-9]+[-]?[0-9]+[-]?[0-9]$")
    @Size(min = MIN_BOOK_ISBN_LENGTH, max = MAX_BOOK_ISBN_LENGTH)
    private String isbn;

    @Size(max = MAX_BOOK_DESCRIPTION_LENGTH)
    private String description;

    @NotNull
    private Genre genre;

    /**
     * Constructor for jackson.
     */
    public CreateBookResource() {
        super();
    }

    /**
     * Constructor.
     *
     * @param title book title
     * @param isbn book isbn number
     * @param description book description
     * @param genre book genre
     */
    public CreateBookResource(String title, String isbn, String description, Genre genre) {
        this(null, title, isbn, description, genre);
    }

    /**
     * Constructor.
     *
     * @param identifier book identifier
     * @param title book title
     * @param isbn book isbn number
     * @param description book description
     * @param genre book genre
     */
    public CreateBookResource(
            @JsonProperty("id") UUID identifier, @JsonProperty("title") String title,
            @JsonProperty("isbn") String isbn, @JsonProperty("description") String description,
            @JsonProperty("genre") Genre genre) {
        this.identifier = identifier;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.genre = genre;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public CreateBookResource setIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateBookResource setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public CreateBookResource setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateBookResource setDescription(String description) {
        this.description = description;
        return this;
    }

    public Genre getGenre() {
        return genre;
    }

    public CreateBookResource setGenre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public Book createBook() {
        return new Book(identifier, title, isbn, description, genre);
    }
}
