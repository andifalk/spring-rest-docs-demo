package com.example.book.entity;

import com.example.author.entity.Author;
import com.example.common.AbstractAuditableEntity;
import com.example.common.StringEnumeration;
import com.example.user.entity.User;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Person entity.
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Book extends AbstractAuditableEntity {

    public static final int MAX_BOOK_TITLE_LENGTH = 100;
    public static final int MIN_BOOK_TITLE_LENGTH = 1;
    public static final int MIN_BOOK_ISBN_LENGTH = 17;
    public static final int MAX_BOOK_ISBN_LENGTH = 26;
    public static final int MAX_BOOK_DESCRIPTION_LENGTH = 2000;
    private static final int MAX_BOOK_GENRE_LENGTH = 100;
    private static final int MAX_BOOK_IDENTIFIER_LENGTH = 50;

    @SuppressWarnings("unused")
    @Version
    @NotNull
    @Column(nullable = false)
    private Long version;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Author> authors = new HashSet<>();

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

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public Book setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Book setDescription(String description) {
        this.description = description;
        return this;
    }

    public Book setGenre(Genre genre) {
        this.genre = genre;
        return this;
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public Book setAuthors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
