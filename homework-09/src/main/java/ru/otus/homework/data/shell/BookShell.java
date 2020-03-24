package ru.otus.homework.data.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.service.BookStorageService;
import ru.otus.homework.data.service.CommentStorageService;

import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
public class BookShell {

    private final BookStorageService bookService;
    private final CommentStorageService commentStorageService;

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

    @ShellMethod(key = "book-comments", value = "load book comments by book id")
    public void bookCommentsByBookId(@ShellOption("--bookId") UUID bookId) {
        var comments = commentStorageService.commentsByBook(bookId);
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
                parsedUuidList(rawAuthorsIds),
                parsedUuidList(rawGenresIds),
                Arrays.asList(rawComments.split("|")));
    }

    @ShellMethod(key = "book-edit-title", value = "edit book title")
    public void editBookTitle(@ShellOption("--book-id") UUID bookId,
                              @ShellOption("--new-title") String newTitle,
                              @ShellOption(value = "--authors-ids",
                                      help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                                          String rawAuthorsIds,
                              @ShellOption(value = "--genres-ids",
                                      help = "example value: <uuid_1>,<uuid_2>,<uuid_3>,...")
                                          String rawGenresIds) {
        bookService.update(bookId, newTitle,
                parsedUuidList(rawAuthorsIds),
                parsedUuidList(rawGenresIds));
    }

    @ShellMethod(key = "book-delete", value = "delete book")
    public void deleteBook(UUID bookId) {
        bookService.deleteBookById(bookId);
    }

    private List<UUID> parsedUuidList(String rawUuidList) {
        return Arrays.stream(rawUuidList.split(","))
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }
}
