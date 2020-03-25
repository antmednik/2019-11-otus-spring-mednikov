package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.dao.BookRepository;
import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenSearchForNonExistingBookThenNotFound(){
        Optional<Book> book = bookRepository.findById(UUID.randomUUID());
        assertThat(book).isEmpty();
    }

    @Test
    public void whenBookSavedThenLoadedById() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId);
        book.setTitle(UUID.randomUUID().toString());
        bookRepository.save(book);

        Optional<Book> storedBook = bookRepository.findById(bookId);

        assertThat(storedBook).isNotEmpty();
        assertThat(storedBook.get()).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void whenNoBooksThenGetAllReturnsEmptyList() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }

    @Test
    public void whenThreeBooksSavedThenThreeLoadedFromDao() {
        List<Book> books = List.of(
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()));

        for (Book book : books){
            bookRepository.save(book);
        }

        List<Book> storedBooks = bookRepository.findAll();

        assertThat(storedBooks).hasSize(books.size());
        for (Book storedBook : storedBooks) {
            Optional<Book> book = books.stream()
                    .filter(g -> g.getId().equals(storedBook.getId()))
                    .findFirst();

            assertThat(book).isNotEmpty();
            assertThat(book.get()).usingRecursiveComparison().isEqualTo(storedBook);
        }
    }

    @Test
    public void whenSavedNullObjThenNPEThrown() {
        assertThatThrownBy(() -> bookRepository.save(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void whenBookFoundThenTitleUpdated() {
        final UUID bookId = UUID.randomUUID();
        final String newTitle = UUID.randomUUID().toString();

        Book book = new Book(bookId, UUID.randomUUID().toString());
        Book savedBook = bookRepository.save(book);

        assertThat(savedBook).usingRecursiveComparison().isEqualTo(book);

        savedBook.setTitle(newTitle);

        Optional<Book> updatedBook = bookRepository.findById(book.getId());

        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void whenNoBookThenNothingToDelete() {
        assertThatThrownBy(() ->bookRepository.deleteById(UUID.randomUUID()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void whenBookFoundThenItCanBeDeleted() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId, UUID.randomUUID().toString());
        bookRepository.save(book);

        Optional<Book> savedBook = bookRepository.findById(bookId);
        assertThat(savedBook).isNotEmpty();

        bookRepository.deleteById(bookId);

        Optional<Book> savedBookAfterDeletion = bookRepository.findById(bookId);
        assertThat(savedBookAfterDeletion).isEmpty();
    }
}
