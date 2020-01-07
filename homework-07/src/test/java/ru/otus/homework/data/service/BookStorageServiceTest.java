package ru.otus.homework.data.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Book;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookStorageServiceTest {

    @Autowired
    private BookStorageService bookStorageService;

    @Autowired
    private GenreStorageService genreStorageService;

    @Autowired
    private AuthorStorageService authorStorageService;

    @Test
    public void whenBookWithGenresAndAuthorsSavedThenFullBookDataLoaded() {
        Book book = bookStorageService.save(UUID.randomUUID().toString());

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

        bookStorageService.saveBookGenreConnection(book.getId(), genre1.getId());
        bookStorageService.saveBookGenreConnection(book.getId(), genre2.getId());
        bookStorageService.saveBookGenreConnection(book.getId(), genre3.getId());

        bookStorageService.saveBookAuthorConnection(book.getId(), author1.getId());
        bookStorageService.saveBookAuthorConnection(book.getId(), author2.getId());

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
        Book book = bookStorageService.save(UUID.randomUUID().toString());

        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        bookStorageService.saveBookGenreConnection(book.getId(), genre1.getId());
        bookStorageService.saveBookGenreConnection(book.getId(), genre2.getId());

        bookStorageService.saveBookAuthorConnection(book.getId(), author1.getId());
        bookStorageService.saveBookAuthorConnection(book.getId(), author2.getId());

        book.getGenres().add(genre1);
        book.getGenres().add(genre2);

        book.getAuthors().add(author1);
        book.getAuthors().add(author2);

        Optional<Book> storedBookWrapper = bookStorageService.bookById(book.getId());
        assertThat(storedBookWrapper).isNotEmpty();
        Book storedBook = storedBookWrapper.get();
        assertThat(storedBook).usingRecursiveComparison().isEqualTo(book);

        bookStorageService.deleteBookGenreConnection(book.getId(), genre2.getId());
        bookStorageService.deleteBookAuthorConnection(book.getId(), author1.getId());

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
        Book book1 = bookStorageService.save(UUID.randomUUID().toString());
        Book book2 = bookStorageService.save(UUID.randomUUID().toString());

        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        bookStorageService.saveBookGenreConnection(book1.getId(), genre1.getId());
        bookStorageService.saveBookGenreConnection(book1.getId(), genre2.getId());
        bookStorageService.saveBookGenreConnection(book2.getId(), genre1.getId());

        bookStorageService.saveBookAuthorConnection(book1.getId(), author1.getId());
        bookStorageService.saveBookAuthorConnection(book2.getId(), author1.getId());
        bookStorageService.saveBookAuthorConnection(book2.getId(), author2.getId());

        book1.setGenres(List.of(genre1, genre2));
        book1.setAuthors(List.of(author1));

        book2.setGenres(List.of(genre1));
        book2.setAuthors(List.of(author1, author2));

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
        Book book = bookStorageService.save(UUID.randomUUID().toString());

        Genre genre1 = genreStorageService.save(UUID.randomUUID().toString());
        Genre genre2 = genreStorageService.save(UUID.randomUUID().toString());

        Author author1 = authorStorageService.save(UUID.randomUUID().toString());
        Author author2 = authorStorageService.save(UUID.randomUUID().toString());

        bookStorageService.saveBookGenreConnection(book.getId(), genre1.getId());
        bookStorageService.saveBookGenreConnection(book.getId(), genre2.getId());

        bookStorageService.saveBookAuthorConnection(book.getId(), author1.getId());
        bookStorageService.saveBookAuthorConnection(book.getId(), author2.getId());

        var storedBook = bookStorageService.bookById(book.getId());
        assertThat(storedBook).isNotEmpty();

        bookStorageService.deleteBookById(book.getId());

        var storedBookAfterDeletion = bookStorageService.bookById(book.getId());
        assertThat(storedBookAfterDeletion).isEmpty();
    }
}
