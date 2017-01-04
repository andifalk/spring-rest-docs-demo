package com.example.book.boundary;

import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.Collection;
import java.util.UUID;

/**
 * Implementation of {@link BookService}.
 */
@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final IdGenerator idGenerator;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, IdGenerator idGenerator) {
        this.bookRepository = bookRepository;
        this.idGenerator = idGenerator;
    }

    @Transactional
    @Override
    public Book createBook(Book book) {
        if (book.getIdentifier() == null) {
            book.setIdentifier(idGenerator.generateId());
        }
        //book.setLastModified(System.currentTimeMillis());
        return this.bookRepository.save(book);
    }

    @Override
    public Book findByIdentifier(UUID identifier) {
        return this.bookRepository.findByIdentifier(identifier);
    }

    @Override
    public Collection<Book> findAllBooks() {
        return this.bookRepository.findAll();
    }

    @Override
    public Collection<Book> findByIsbn(String isbn) {
        return this.bookRepository.findByIsbnLike(isbn);
    }

    @Override
    public Collection<Book> findByTitle(String title) {
        return this.bookRepository.findByTitleLike(title);
    }

    @Transactional
    @Override
    public boolean deleteBook(UUID identifier) {
        long count = this.bookRepository.deleteByIdentifier(identifier);
        return count > 0;
    }

    @Override
    public Book updateBook(Book book) {
        //book.setLastModified(System.currentTimeMillis());
        return this.bookRepository.save(book);
    }
}
