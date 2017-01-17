package com.example;

import com.example.author.boundary.AuthorService;
import com.example.author.entity.Author;
import com.example.author.entity.Gender;
import com.example.book.boundary.BookService;
import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.example.user.boundary.UserService;
import com.example.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

/**
 * Creates some sample data for application.
 */
@Service
public class SampleDataInitializer implements SmartLifecycle {

    private final AuthorService authorService;

    private final BookService bookService;

    private final UserService userService;

    private boolean running;

    @Autowired
    public SampleDataInitializer(AuthorService authorService, BookService bookService, UserService userService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public void start() {
        running = true;

        userService.createUser(
                new User("Hans", "Mustermann", "user", "secret"));
        userService.createUser(
                new User("System", "System", "technical", "secret"));

        Author stephenKing = authorService.createAuthor(new Author(Gender.MALE, "Stephen", "King"));

        Book es = new Book("ES", "978-3-4534-3577-3",
                "ES ist ein namenloses Grauen, das die amerikanische Kleinstadt Derry in Maine seit Jahrhunderten immer wieder heimsucht." +
                        "Im Abstand von ungefähr 30 Jahren wütet ES gnadenlos und in immer neuer Gestalt unter den Einwohnern der ansonsten so idyllischen Kleinstadt, ES mordet auf brutalste Art und Weise - und zwar bevorzugt Kinder." +
                        "Als ES 1958 wieder zuschlägt, vermuten Polizei und Bürger darin das Werk eines irren Serientäters. Nur eine Gruppe von sieben Kindern weiß es besser, denn sie alle haben schon mindestens eine traumatische Begegnung mit IHM hinter sich.",
                Genre.HORROR);
        es.getAuthors().add(stephenKing);
        bookService.createBook(es);

        running = false;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
