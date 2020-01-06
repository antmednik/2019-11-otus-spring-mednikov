package ru.otus.homework.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.data.dao.*;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Book;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BookStorageService {

    private final BookDao bookDao;
    private final BookAuthorDao bookAuthorDao;
    private final AuthorDao authorDao;
    private final BookGenreDao bookGenreDao;
    private final GenreDao genreDao;

    public void save(String title){
        Book book = new Book(UUID.randomUUID());
        book.setTitle(title);
        bookDao.save(book);
    }

    public void saveBookAuthorConnection(UUID bookId, UUID authorId) {
        bookAuthorDao.saveBookAuthorConnection(bookId, authorId);
    }

    public void deleteBookAuthorConnection(UUID bookId, UUID authorId) {
        bookAuthorDao.deleteBookAuthorConnection(bookId, authorId);
    }

    public void saveBookGenreConnection(UUID bookId, UUID genreId) {
        bookGenreDao.saveBookGenreConnection(bookId, genreId);
    }

    public void deleteBookGenreConnection(UUID bookId, UUID genreId) {
        bookGenreDao.deleteBookGenreConnection(bookId, genreId);
    }

    public List<Book> books() {
        List<Book> books = bookDao.books();

        for (Book book : books) {
            enrich(book);
        }

        return books;
    }

    public Optional<Book> bookById(UUID bookId) {
        Optional<Book> book = bookDao.bookById(bookId);
        if (book.isEmpty()) {
            return Optional.empty();
        }

        Book bookObj = book.get();
        enrich(bookObj);

        return Optional.of(bookObj);
    }

    public void deleteById(UUID bookId){
        bookAuthorDao.deleteBookAuthorConnection(bookId);

        bookGenreDao.deleteBookGenreConnection(bookId);

        bookDao.deleteById(bookId);
    }

    public void updateTitle(UUID bookId, String newTitle){
        bookDao.updateTitle(bookId, newTitle);
    }

    private void enrich(Book book){
        book.setAuthors(bookAuthorDao.authorsOfBook(book.getId()));
        book.setGenres(bookGenreDao.genresOfBook(book.getId()));
    }
}
