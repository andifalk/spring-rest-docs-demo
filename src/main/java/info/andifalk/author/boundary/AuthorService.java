package info.andifalk.author.boundary;

import info.andifalk.author.entity.Author;

import java.util.Collection;
import java.util.UUID;

/**
 * Service to manage {@link Author authors}.
 */
public interface AuthorService {

    /**
     * Creates a new {@link Author author}.
     *
     * @param author author to create
     * @return created persistent author
     */
    Author createAuthor(Author author);

    /**
     * Finds unique author for given identifier.
     *
     * @param identifier identifier for author to find
     * @return author or null if none found
     */
    Author findByIdentifier(UUID identifier);

    /**
     * Returns all persistent authors.
     *
     * @return collection of {@link Author authors}
     */
    Collection<Author> findAllAuthors();

    /**
     * Finds all authors matching the given last name.
     *
     * @param lastName last name to search for
     * @return collection of {@link Author authors}
     */
    Collection<Author> findByLastname(String lastName);

    /**
     * Deletes author with given identifier.
     *
     * @param identifier identifier of author to delete
     * @return <code>true</code> if author has been deleted successfully
     */
    boolean deleteAuthor(UUID identifier);
}
