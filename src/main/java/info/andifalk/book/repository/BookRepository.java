package info.andifalk.book.repository;

import info.andifalk.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

/**
 * Repository for {@link Book books}.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    Collection<Book> findByIsbnLike(String isbn);

    Collection<Book> findByTitleLike(String title);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.identifier = :identifier")
    long deleteByIdentifier(@Param("identifier") UUID identifier);

    Book findByIdentifier(UUID identifier);
}
