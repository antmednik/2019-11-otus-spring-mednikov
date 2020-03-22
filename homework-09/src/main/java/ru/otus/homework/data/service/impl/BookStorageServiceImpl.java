package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.AuthorRepository;
import ru.otus.homework.data.dao.BookRepository;
import ru.otus.homework.data.dao.GenreRepository;
import ru.otus.homework.data.entity.Author;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Genre;
import ru.otus.homework.data.service.BookStorageService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookStorageServiceImpl implements BookStorageService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Book save(String title, List<UUID> authorsIds, List<UUID> genresIds) {

        Book book = new Book(UUID.randomUUID(), title);

        List<Author> authors = authorRepository.authors(authorsIds);
        List<Genre> genres = genreRepository.genres(genresIds);

        book.setGenres(new HashSet<>(genres));
        book.setAuthors(new HashSet<>(authors));

        return bookRepository.save(book);
    }

    @Override
    public List<Book> books() {
        return bookRepository.books();
    }

    @Override
    public Optional<Book> bookById(UUID bookId) {
        return bookRepository.bookById(bookId);
    }

    @Override
    public boolean deleteBookById(UUID bookId){
        return bookRepository.deleteById(bookId);
    }

    @Override
    @Transactional
    public boolean update(UUID bookId, String newTitle, List<UUID> authorsIds, List<UUID> genresIds) {
        Book book = bookRepository.bookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found."));
        book.setTitle(newTitle);

        List<Author> authors = authorRepository.authors(authorsIds);
        List<Genre> genres = genreRepository.genres(genresIds);

        book.setGenres(new HashSet<>(genres));
        book.setAuthors(new HashSet<>(authors));

        bookRepository.save(book);

        return true;
    }
}
