package ru.otus.homework.data.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.homework.data.dao.AuthorRepository;
import ru.otus.homework.data.dao.BookRepository;
import ru.otus.homework.data.dao.GenreRepository;
import ru.otus.homework.data.entity.Author;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Genre;
import ru.otus.homework.data.service.impl.BookStorageServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BookStorageServiceImplTest {

    private BookStorageServiceImpl bookStorageService;

    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private AuthorRepository authorRepository;

    private EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    public void beforeEach() {
        bookRepository = mock(BookRepository.class);
        genreRepository = mock(GenreRepository.class);
        authorRepository = mock(AuthorRepository.class);

        bookStorageService = new BookStorageServiceImpl(bookRepository,
                genreRepository, authorRepository);
    }

    @Test
    public void whenBookWithGenresAndAuthorsSavedThenOk() {
        final String bookTitle = UUID.randomUUID().toString();
        final List<UUID> authorIds = List.of(UUID.randomUUID());
        final List<UUID> genreIds = List.of(UUID.randomUUID());
        final List<Author> authors = List.of(easyRandom.nextObject(Author.class));
        final List<Genre> genres = List.of(easyRandom.nextObject(Genre.class));

        when(genreRepository.genres(genreIds)).thenReturn(genres);
        when(authorRepository.authors(authorIds)).thenReturn(authors);

        Book book = bookStorageService.save(bookTitle, authorIds, genreIds);

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1))
                .save(argumentCaptor.capture());

        var savedBook = argumentCaptor.getValue();

        assertThat(savedBook.getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.getGenres()).containsExactlyInAnyOrderElementsOf(genres);
        assertThat(savedBook.getAuthors()).containsExactlyInAnyOrderElementsOf(authors);

        verify(authorRepository, times(1)).authors(authorIds);
        verify(genreRepository, times(1)).genres(genreIds);
    }

    @Test
    public void whenBookWithGenresAndAuthorsUpdatedThenOk() {
        final UUID bookId = UUID.randomUUID();
        final String bookTitle = UUID.randomUUID().toString();
        final List<UUID> authorIds = List.of(UUID.randomUUID());
        final List<UUID> genreIds = List.of(UUID.randomUUID());
        final List<Author> authors = List.of(easyRandom.nextObject(Author.class));
        final List<Genre> genres = List.of(easyRandom.nextObject(Genre.class));
        final Book book = easyRandom.nextObject(Book.class);

        when(bookRepository.bookById(bookId)).thenReturn(Optional.of(book));

        when(genreRepository.genres(genreIds)).thenReturn(genres);
        when(authorRepository.authors(authorIds)).thenReturn(authors);

        bookStorageService.update(bookId, bookTitle, authorIds, genreIds);

        verify(bookRepository, times(1))
                .bookById(bookId);

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1))
                .save(argumentCaptor.capture());

        var savedBook = argumentCaptor.getValue();

        assertThat(savedBook.getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.getGenres()).containsExactlyInAnyOrderElementsOf(genres);
        assertThat(savedBook.getAuthors()).containsExactlyInAnyOrderElementsOf(authors);

        verify(authorRepository, times(1)).authors(authorIds);
        verify(genreRepository, times(1)).genres(genreIds);
    }
}
