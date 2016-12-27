package com.example.author.repository;

import com.example.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

/**
 * Repository for {@link Author authors}.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Finds an {@link Author author} by its identifier.
     *
     * @param identifier identifier to find an author
     * @return the author or null if none found
     */
    Author findByIdentifier(UUID identifier);

    boolean deleteByIdentifier(UUID identifier);

    Collection<Author> findByLastname(String lastName);
}
