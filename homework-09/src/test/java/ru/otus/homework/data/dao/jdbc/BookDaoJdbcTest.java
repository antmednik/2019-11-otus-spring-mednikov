package ru.otus.homework.data.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Test
    public void whenSearchForNonExistingBookThenNotFound(){
        Optional<Book> book = bookDaoJdbc.bookById(UUID.randomUUID());
        assertThat(book).isEmpty();
    }

    @Test
    public void whenBookSavedThenLoadedById() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId);
        book.setTitle(UUID.randomUUID().toString());
        bookDaoJdbc.save(book);

        Optional<Book> storedBook = bookDaoJdbc.bookById(bookId);

        assertThat(storedBook).isNotEmpty();
        assertThat(storedBook.get()).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void whenNoBooksThenGetAllReturnsEmptyList() {
        List<Book> books = bookDaoJdbc.books();
        assertThat(books).hasSize(0);
    }

    @Test
    public void whenThreeBooksSavedThenThreeLoadedFromDao() {
        List<Book> books = List.of(
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Book(UUID.randomUUID(), UUID.randomUUID().toString()));

        for (Book book : books){
            bookDaoJdbc.save(book);
        }

        List<Book> storedBooks = bookDaoJdbc.books();

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
        assertThatThrownBy(() -> bookDaoJdbc.save(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void whenBookNotFoundThenNoUpdateDone() {
        assertThat(bookDaoJdbc.updateTitle(UUID.randomUUID(), ""))
                .isFalse();
    }

    @Test
    public void whenBookFoundThenTitleUpdated() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId, UUID.randomUUID().toString());
        Book savedBook = bookDaoJdbc.save(book);

        assertThat(savedBook).usingRecursiveComparison().isEqualTo(book);

        final String newTitle = UUID.randomUUID().toString();
        boolean updated = bookDaoJdbc.updateTitle(savedBook.getId(), newTitle);
        assertThat(updated).isTrue();

        Optional<Book> updatedBook = bookDaoJdbc.bookById(book.getId());

        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void whenNoBookThenNothingToDelete() {
        assertThat(bookDaoJdbc.deleteById(UUID.randomUUID())).isFalse();
    }

    @Test
    public void whenBookFoundThenItCanBeDeleted() {
        final UUID bookId = UUID.randomUUID();
        Book book = new Book(bookId, UUID.randomUUID().toString());
        bookDaoJdbc.save(book);

        Optional<Book> savedBook = bookDaoJdbc.bookById(bookId);
        assertThat(savedBook).isNotEmpty();

        assertThat(bookDaoJdbc.deleteById(bookId)).isTrue();

        Optional<Book> savedBookAfterDeletion = bookDaoJdbc.bookById(bookId);
        assertThat(savedBookAfterDeletion).isEmpty();
    }
}
