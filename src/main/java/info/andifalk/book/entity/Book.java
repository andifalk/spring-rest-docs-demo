package info.andifalk.book.entity;

import info.andifalk.common.StringEnumeration;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Person entity.
 */
@Entity
public class Book extends AbstractPersistable<Long> {

    public static final int MAX_BOOK_IDENTIFIER_LENGTH = 50;
    public static final int MAX_BOOK_TITLE_LENGTH = 100;
    public static final int MIN_BOOK_TITLE_LENGTH = 1;
    public static final int MAX_BOOK_GENRE_LENGTH = 100;
    public static final int MIN_BOOK_ISBN_LENGTH = 17;
    public static final int MAX_BOOK_ISBN_LENGTH = 26;
    public static final int MAX_BOOK_DESCRIPTION_LENGTH = 2000;

    @NotNull
    @Column(nullable = false, length = MAX_BOOK_IDENTIFIER_LENGTH)
    private UUID identifier;

    @NotNull
    @Size(min = MIN_BOOK_TITLE_LENGTH, max = MAX_BOOK_TITLE_LENGTH)
    @Column(nullable = false, length = MAX_BOOK_TITLE_LENGTH)
    private String title;

    @NotNull
    @Pattern(regexp = "^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-]){4})[-0-9]{17}$)" +
            "97[89][-]?[0-9]{1,5}[-]?[0-9]+[-]?[0-9]+[-]?[0-9]$")
    @Size(min = MIN_BOOK_ISBN_LENGTH, max = MAX_BOOK_ISBN_LENGTH)
    @Column(nullable = false, length = MAX_BOOK_ISBN_LENGTH)
    private String isbn;

    @Size(max = MAX_BOOK_DESCRIPTION_LENGTH)
    @Column(length = MAX_BOOK_DESCRIPTION_LENGTH)
    private String description;

    @NotNull
    @StringEnumeration(enumClass = Genre.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_BOOK_GENRE_LENGTH)
    private Genre genre;

    /**
     * For JPA.
     */
    public Book() {
        super();
    }

    /**
     * Constructor.
     *
     * @param title book title (mandatory)
     * @param isbn ISBN number (mandatory)
     * @param description book description (optional)
     * @param genre book genre (mandatory)
     */
    public Book(String title, String isbn, String description, Genre genre) {
        this(null, title, isbn, description, genre);
    }

    /**
     * Constructor.
     *
     * @param identifier book identifier (optional, will be generated if not set)
     * @param title book title (mandatory)
     * @param isbn ISBN number (mandatory)
     * @param description book description (optional)
     * @param genre book genre (mandatory)
     */
    public Book(UUID identifier, String title, String isbn, String description, Genre genre) {
        this.identifier = identifier;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public Genre getGenre() {
        return genre;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public Book setIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("identifier", identifier)
                .append("title", title)
                .append("isbn", isbn)
                .append("description", description)
                .append("genre", genre)
                .toString();
    }
}
