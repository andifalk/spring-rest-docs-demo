package com.example.author.boundary;

import com.example.author.entity.Author;
import com.example.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.Collection;
import java.util.UUID;

/**
 * Implementation for {@link AuthorService}.
 */
@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final IdGenerator idGenerator;

    /**
     * Constructor.
     *
     * @param authorRepository the {@link AuthorRepository}
     * @param idGenerator the {@link IdGenerator}
     */
    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, IdGenerator idGenerator) {
        this.authorRepository = authorRepository;
        this.idGenerator = idGenerator;
    }

    @Transactional
    @Override
    public Author createAuthor(Author author) {
        if (author.getIdentifier() == null) {
            author.setIdentifier(idGenerator.generateId());
        }
        return this.authorRepository.save(author);
    }

    @Override
    public Author findByIdentifier(UUID identifier) {
        return this.authorRepository.findByIdentifier(identifier);
    }

    @Override
    public Collection<Author> findAllAuthors() {
        return this.authorRepository.findAll();
    }

    @Override
    public Collection<Author> findByLastname(String lastName) {
        return this.authorRepository.findByLastname(lastName);
    }

    @Transactional
    @Override
    public boolean deleteAuthor(UUID identifier) {
        return this.authorRepository.deleteByIdentifier(identifier);
    }
}
