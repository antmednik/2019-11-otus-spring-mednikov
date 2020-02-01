package ru.otus.homework.data.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Genre;
import ru.otus.homework.data.service.impl.BookStorageServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookStorageServiceImplTest {

    @Autowired
    private BookStorageServiceImpl bookStorageService;

    @Autowired
    private GenreStorageService genreStorageService;

    @Autowired
    private AuthorStorageService authorStorageService;

    @Test
    public void whenBookWithGenresAndAuthorsSavedThenFullBookDataLoaded() {
        Book book = bookStorageService.save(UUID.randomUUID().toString(),
                Collections.emptyList(), Collections.emptyList());

        Optional<Book> storedBookWrapper = bookStorageService.bookById(book.getId());
        assertThat(storedBookWrapper).isNotEmpty();
        Book storedBook = storedBookWrapper.get();
        assertThat(storedBook).usingRecursiveComparison().isEqualTo(book);
        assertThat(storedBook.getAuthors()).hasSize(0);
        assertThat(storedBook.getGenres()).hasSize(0);

        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre3 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        bookStorageService.update(book.getId(), book.getTitle(),
            List.of(author1.getId(), author2.getId()),
            List.of(genre1.getId(), genre2.getId(), genre3.getId()));

        Optional<Book> storedFinalBookWrapper = bookStorageService.bookById(book.getId());
        assertThat(storedFinalBookWrapper).isNotEmpty();
        Book storedFinalBook = storedFinalBookWrapper.get();

        book.getGenres().add(genre1);
        book.getGenres().add(genre2);
        book.getGenres().add(genre3);

        book.getAuthors().add(author1);
        book.getAuthors().add(author2);

        assertThat(storedFinalBook).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void whenBookWithGenresAndAuthorsSavedThenGenreAndAuthorDeletedAndLoadedBookDataChanged() {
        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        Book book = bookStorageService.save(UUID.randomUUID().toString(),
                List.of(author1.getId(), author2.getId()),
                List.of(genre1.getId(), genre2.getId()));

        Optional<Book> storedBookWrapper = bookStorageService.bookById(book.getId());
        assertThat(storedBookWrapper).isNotEmpty();
        Book storedBook = storedBookWrapper.get();
        assertThat(storedBook).usingRecursiveComparison().isEqualTo(book);

        boolean updated = bookStorageService.update(book.getId(), book.getTitle(),
                List.of(author2.getId()),
                List.of(genre1.getId()));
        assertThat(updated).isTrue();

        book.setGenres(List.of(genre1));
        book.setAuthors(List.of(author2));

        Optional<Book> storedBookAfterDeletionsWrapper = bookStorageService.bookById(book.getId());
        assertThat(storedBookAfterDeletionsWrapper).isNotEmpty();
        Book storedBookAfterDeletions = storedBookAfterDeletionsWrapper.get();
        assertThat(storedBookAfterDeletions).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void whenNoBooksStoredThenEmptyListLoaded() {
        List<Book> books = bookStorageService.books();
        assertThat(books).hasSize(0);
    }

    @Test
    public void whenTwoBooksStoredThenTwoBooksLoaded() {
        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        Book book1 = bookStorageService.save(UUID.randomUUID().toString(),
                List.of(author1.getId()),
                List.of(genre1.getId(), genre2.getId()));
        Book book2 = bookStorageService.save(UUID.randomUUID().toString(),
                List.of(author1.getId(), author2.getId()),
                List.of(genre1.getId()));

        List<Book> books = List.of(book1, book2);

        List<Book> storedBooks = bookStorageService.books();
        assertThat(storedBooks).hasSize(books.size());

        for (Book storedBook : storedBooks) {
            var book = books.stream().filter(b -> b.getId().equals(storedBook.getId())).findFirst();
            assertThat(book).isNotEmpty();
            assertThat(book.get()).usingRecursiveComparison().isEqualTo(storedBook);
        }
    }

    @Test
    public void whenBookStoredThenAfterDeleteNoBook() {
        Book book = bookStorageService.save(UUID.randomUUID().toString(),
                Collections.emptyList(), Collections.emptyList());

        var storedBook = bookStorageService.bookById(book.getId());
        assertThat(storedBook).isNotEmpty();

        bookStorageService.deleteBookById(book.getId());

        var storedBookAfterDeletion = bookStorageService.bookById(book.getId());
        assertThat(storedBookAfterDeletion).isEmpty();
    }
}
