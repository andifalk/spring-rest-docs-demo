package com.example;

import com.example.book.boundary.BookService;
import com.example.book.entity.Book;
import com.example.author.boundary.AuthorService;
import com.example.author.entity.Author;
import com.example.author.entity.Gender;
import com.example.book.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Creates some sample data for application.
 */
@Service
public class SampleDataInitializer implements CommandLineRunner {

    private final AuthorService authorService;

    private final BookService bookService;

    @Autowired
    public SampleDataInitializer(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        Author stephenKing = authorService.createAuthor(new Author(Gender.MALE, "Stephen", "King"));

        Book es = bookService.createBook(new Book("ES", "978-3-4534-3577-3",
                "ES ist ein namenloses Grauen, das die amerikanische Kleinstadt Derry in Maine seit Jahrhunderten immer wieder heimsucht." +
                "Im Abstand von ungefähr 30 Jahren wütet ES gnadenlos und in immer neuer Gestalt unter den Einwohnern der ansonsten so idyllischen Kleinstadt, ES mordet auf brutalste Art und Weise - und zwar bevorzugt Kinder." +
                "Als ES 1958 wieder zuschlägt, vermuten Polizei und Bürger darin das Werk eines irren Serientäters. Nur eine Gruppe von sieben Kindern weiß es besser, denn sie alle haben schon mindestens eine traumatische Begegnung mit IHM hinter sich.",
                Genre.HORROR));
    }
}
