package ru.otus.homework.data.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.data.object.Book;
import ru.otus.homework.data.service.BookStorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
public class BookShell {

    private final BookStorageService bookService;

    @ShellMethod(key = "books", value = "load all books")
    public void books() {
        List<Book> books = bookService.books();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @ShellMethod(key = "book-by-id", value = "load book by id")
    public void bookById(@ShellOption("--id") UUID id) {
        Optional<Book> book = bookService.bookById(id);
        System.out.println(book);
    }

    @ShellMethod(key = "book-save", value = "save new book")
    public void saveBook(@ShellOption("--title") String title) {
        bookService.save(title);
    }

    @ShellMethod(key = "book-add-author", value = "connect book with author")
    public void addBookAuthorConnection(@ShellOption("--book-id") UUID bookId,
                                        @ShellOption("--author-id") UUID authorId) {
        bookService.saveBookAuthorConnection(bookId, authorId);
    }

    @ShellMethod(key = "book-delete-author", value = "disconnect book with author")
    public void deleteBookAuthorConnection(@ShellOption("--book-id") UUID bookId,
                                        @ShellOption("--author-id") UUID authorId) {
        bookService.deleteBookAuthorConnection(bookId, authorId);
    }

    @ShellMethod(key = "book-add-genre", value = "connect book with genre")
    public void addBookGenreConnection(@ShellOption("--book-id") UUID bookId,
                                       @ShellOption("--genre-id") UUID genreId) {
        bookService.saveBookGenreConnection(bookId, genreId);
    }

    @ShellMethod(key = "book-delete-genre", value = "disconnect book with genre")
    public void deleteBookGenreConnection(@ShellOption("--book-id") UUID bookId,
                                       @ShellOption("--genre-id") UUID genreId) {
        bookService.deleteBookGenreConnection(bookId, genreId);
    }

    @ShellMethod(key = "book-edit-title", value = "edit book title")
    public void editBookTitle(@ShellOption("--book-id") UUID bookId,
                              @ShellOption("--new-title") String newTitle) {
        bookService.updateTitle(bookId, newTitle);
    }

    @ShellMethod(key = "book-delete", value = "delete book")
    public void deleteBook(UUID bookId) {
        bookService.deleteBookById(bookId);
    }
}
