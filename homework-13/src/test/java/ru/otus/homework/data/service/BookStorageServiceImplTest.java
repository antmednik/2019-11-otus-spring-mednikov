package ru.otus.homework.data.service;

import org.bson.types.ObjectId;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.data.dao.mongo.AuthorRepository;
import ru.otus.homework.data.dao.mongo.BookRepository;
import ru.otus.homework.data.dao.mongo.GenreRepository;
import ru.otus.homework.data.entity.mongo.Author;
import ru.otus.homework.data.entity.mongo.Book;
import ru.otus.homework.data.entity.mongo.Genre;
import ru.otus.homework.data.service.impl.BookStorageServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookStorageServiceImplTest {

    @InjectMocks
    private BookStorageServiceImpl bookStorageService;

    @Mock private BookRepository bookRepository;
    @Mock private GenreRepository genreRepository;
    @Mock private AuthorRepository authorRepository;

    private EasyRandom easyRandom = new EasyRandom();

    @Test
    public void whenBookWithGenresAndAuthorsSavedThenOk() {
        final String bookTitle = UUID.randomUUID().toString();
        final List<String> authorIds = List.of(easyRandom.nextObject(ObjectId.class).toString());
        final List<String> genreIds = List.of(easyRandom.nextObject(ObjectId.class).toString());
        final List<Author> authors = List.of(easyRandom.nextObject(Author.class));
        final List<Genre> genres = List.of(easyRandom.nextObject(Genre.class));
        final List<String> comments = List.of(UUID.randomUUID().toString());

        when(genreRepository.findAllById(any())).thenReturn(genres);
        when(authorRepository.findAllById(any())).thenReturn(authors);

        Book book = bookStorageService.save(bookTitle, authorIds, genreIds, comments);

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1))
                .save(argumentCaptor.capture());

        var savedBook = argumentCaptor.getValue();

        assertThat(savedBook.getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.getGenres()).containsExactlyInAnyOrderElementsOf(genres);
        assertThat(savedBook.getAuthors()).containsExactlyInAnyOrderElementsOf(authors);

        verify(authorRepository, times(1)).findAllById(any());
        verify(genreRepository, times(1)).findAllById(any());
    }

    @Test
    public void whenBookWithGenresAndAuthorsUpdatedThenOk() {
        final String bookId = easyRandom.nextObject(ObjectId.class).toString();
        final String bookTitle = UUID.randomUUID().toString();
        final List<String> authorIds = List.of(easyRandom.nextObject(ObjectId.class).toString());
        final List<String> genreIds = List.of(easyRandom.nextObject(ObjectId.class).toString());
        final List<Author> authors = List.of(easyRandom.nextObject(Author.class));
        final List<Genre> genres = List.of(easyRandom.nextObject(Genre.class));
        final Book book = easyRandom.nextObject(Book.class);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));

        when(genreRepository.findAllById(any())).thenReturn(genres);
        when(authorRepository.findAllById(any())).thenReturn(authors);

        bookStorageService.update(bookId, bookTitle, authorIds, genreIds);

        verify(bookRepository, times(1))
                .findById(any());

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1))
                .save(argumentCaptor.capture());

        var savedBook = argumentCaptor.getValue();

        assertThat(savedBook.getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.getGenres()).containsExactlyInAnyOrderElementsOf(genres);
        assertThat(savedBook.getAuthors()).containsExactlyInAnyOrderElementsOf(authors);

        verify(authorRepository, times(1)).findAllById(any());
        verify(genreRepository, times(1)).findAllById(any());
    }
}
