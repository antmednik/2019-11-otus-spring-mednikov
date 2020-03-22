package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Test
    public void whenSearchForNonExistingBookThenNotFound(){
        Optional<Book> book = bookRepositoryJpa.bookById(UUID.randomUUID());
        assertThat(book).isEmpty();
    }

    @Test
    public void whenBookSavedThenLoadedById() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId);
        book.setTitle(UUID.randomUUID().toString());
        bookRepositoryJpa.save(book);

        Optional<Book> storedBook = bookRepositoryJpa.bookById(bookId);

        assertThat(storedBook).isNotEmpty();
        assertThat(storedBook.get()).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void whenNoBooksThenGetAllReturnsEmptyList() {
        List<Book> books = bookRepositoryJpa.books();
        assertThat(books).hasSize(0);
    }

    @Test
    public void whenThreeBooksSavedThenThreeLoadedFromDao() {
        List<Book> books = List.of(
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()));

        for (Book book : books){
            bookRepositoryJpa.save(book);
        }

        List<Book> storedBooks = bookRepositoryJpa.books();

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
        assertThatThrownBy(() -> bookRepositoryJpa.save(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void whenBookNotFoundThenNoUpdateDone() {
        assertThat(bookRepositoryJpa.updateTitle(UUID.randomUUID(), ""))
                .isFalse();
    }

    @Test
    public void whenBookFoundThenTitleUpdated() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId, UUID.randomUUID().toString());
        Book savedBook = bookRepositoryJpa.save(book);

        assertThat(savedBook).usingRecursiveComparison().isEqualTo(book);

        final String newTitle = UUID.randomUUID().toString();
        boolean updated = bookRepositoryJpa.updateTitle(savedBook.getId(), newTitle);
        assertThat(updated).isTrue();

        Optional<Book> updatedBook = bookRepositoryJpa.bookById(book.getId());

        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void whenNoBookThenNothingToDelete() {
        assertThat(bookRepositoryJpa.deleteById(UUID.randomUUID())).isFalse();
    }

    @Test
    public void whenBookFoundThenItCanBeDeleted() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId, UUID.randomUUID().toString());
        bookRepositoryJpa.save(book);

        Optional<Book> savedBook = bookRepositoryJpa.bookById(bookId);
        assertThat(savedBook).isNotEmpty();

        assertThat(bookRepositoryJpa.deleteById(bookId)).isTrue();

        Optional<Book> savedBookAfterDeletion = bookRepositoryJpa.bookById(bookId);
        assertThat(savedBookAfterDeletion).isEmpty();
    }
}
