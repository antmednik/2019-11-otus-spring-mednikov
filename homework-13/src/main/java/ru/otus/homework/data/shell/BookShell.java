package ru.otus.homework.data.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.data.entity.mongo.Author;
import ru.otus.homework.data.entity.mongo.Book;
import ru.otus.homework.data.service.BookStorageService;

import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
public class BookShell {

    private final BookStorageService bookService;

    @ShellMethod(key = "authors-with-books", value = "load all authors with books")
    public void authorsWithBooks() {
        var books = bookService.books();
        Map<Author, List<Book>> authorToBooks = new HashMap<>();
        for (Book book : books) {
            for (Author author : book.getAuthors()) {
                authorToBooks.computeIfAbsent(author, (k) -> new ArrayList<>())
                        .add(book);
            }
        }
        System.out.println(authorToBooks);
    }

    @ShellMethod(key = "books", value = "load all books")
    public void books() {
        List<Book> books = bookService.books();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @ShellMethod(key = "book-by-id", value = "load book by id")
    public void bookById(@ShellOption("--id") String id) {
        Optional<Book> book = bookService.bookById(id);
        System.out.println(book);
    }

    @ShellMethod(key = "book-comments", value = "load book comments by book id")
    public void bookCommentsByBookId(@ShellOption("--bookId") String bookId) {
        var comments = bookService.bookById(bookId).orElseThrow().getComments();
        System.out.println(comments);
    }

    @ShellMethod(key = "book-save", value = "save new book")
    public void saveBook(@ShellOption("--title") String title,
                         @ShellOption(value = "--authors-ids",
                                 help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                         String rawAuthorsIds,
                         @ShellOption(value = "--genres-ids",
                                 help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                         String rawGenresIds,
                         @ShellOption(value = "--comments",
                                 help = "example value: <comment_1>|<comment_2>|<comment_3>|...")
                         String rawComments) {

        bookService.save(title,
                parsedStringList(rawAuthorsIds),
                parsedStringList(rawGenresIds),
                parsedStringList(rawComments));
    }

    @ShellMethod(key = "book-edit-title", value = "edit book title")
    public void editBookTitle(@ShellOption("--book-id") String bookId,
                              @ShellOption("--new-title") String newTitle,
                              @ShellOption(value = "--authors-ids",
                                      help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                                          String rawAuthorsIds,
                              @ShellOption(value = "--genres-ids",
                                      help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                                          String rawGenresIds) {
        bookService.update(bookId, newTitle,
                parsedStringList(rawAuthorsIds),
                parsedStringList(rawGenresIds));
    }

    @ShellMethod(key = "book-delete", value = "delete book")
    public void deleteBook(String bookId) {
        bookService.deleteBookById(bookId);
    }

    private List<String> parsedStringList(String rawUuidList) {
        return Arrays.stream(rawUuidList.split(","))
                .collect(Collectors.toList());
    }
}
