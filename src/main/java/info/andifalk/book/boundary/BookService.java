package info.andifalk.book.boundary;

import info.andifalk.book.entity.Book;

import java.util.Collection;
import java.util.UUID;

/**
 * Service to manage {@link Book books}.
 */
public interface BookService {

    /**
     * Creates a new {@link Book book}.
     *
     * @param book book to create
     * @return created persistent book
     */
    Book createBook(Book book);

    /**
     * Finds unique book for given identifier.
     *
     * @param identifier identifier for book to find
     * @return book or null if none found
     */
    Book findByIdentifier(UUID identifier);

    /**
     * Returns all persistent books.
     *
     * @return collection of {@link Book books}
     */
    Collection<Book> findAllBooks();

    /**
     * Finds all book matching the given isbn number.
     *
     * @param isbn isbn number
     * @return collection of {@link Book books}
     */
    Collection<Book> findByIsbn(String isbn);

    /**
     * Finds all books matching the given title.
     *
     * @param title title for books to find
     * @return collection of {@link Book books}
     */
    Collection<Book> findByTitle(String title);

    /**
     * Deletes book with given identifier.
     *
     * @param identifier identifier of book to delete
     * @return <code>true</code> if book has been deleted successfully
     */
    boolean deleteBook(UUID identifier);

}
