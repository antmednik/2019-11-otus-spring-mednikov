package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.mongo.Book;
import ru.otus.homework.data.entity.mongo.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenSearchForNonExistingBookThenNotFound() {
        Optional<Book> book = bookRepository.findById(new ObjectId());
        assertThat(book).isEmpty();
    }

    @Test
    public void whenBookSavedThenLoadedById() {
        Book book = new Book(UUID.randomUUID().toString());
        var savedBook = bookRepository.save(book);

        Optional<Book> storedBook = bookRepository.findById(savedBook.getId());

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
                new Book(UUID.randomUUID().toString()),
                new Book(UUID.randomUUID().toString()),
                new Book(UUID.randomUUID().toString()));

        for (Book book : books) {
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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenBookFoundThenTitleUpdated() {
        final String newTitle = UUID.randomUUID().toString();

        Book book = new Book(UUID.randomUUID().toString());
        Book savedBook = bookRepository.save(book);

        assertThat(savedBook).usingRecursiveComparison().isEqualTo(book);

        savedBook.setTitle(newTitle);
        bookRepository.save(savedBook);

        Optional<Book> updatedBook = bookRepository.findById(book.getId());

        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void whenNoBookThenNothingToDelete() {
        bookRepository.deleteById(new ObjectId());

        assertThat(true).isTrue();
    }

    @Test
    public void whenBookFoundThenItCanBeDeleted() {
        Book book = new Book(UUID.randomUUID().toString());
        var savedBook = bookRepository.save(book);

        Optional<Book> storedBook = bookRepository.findById(savedBook.getId());
        assertThat(storedBook).isNotEmpty();

        bookRepository.deleteById(savedBook.getId());

        Optional<Book> savedBookAfterDeletion = bookRepository.findById(savedBook.getId());
        assertThat(savedBookAfterDeletion).isEmpty();
    }

    @Test
    public void whenThreeCommentsSavedThenThreeLoadedFromDao() {

        final List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(UUID.randomUUID().toString()));
        comments.add(new Comment(UUID.randomUUID().toString()));
        comments.add(new Comment(UUID.randomUUID().toString()));

        var book = new Book(UUID.randomUUID().toString());
        book.getComments().addAll(comments);

        var savedBook = bookRepository.save(book);
        assertThat(savedBook).isNotNull();

        var storedComments = bookRepository.findById(savedBook.getId())
                .orElseThrow().getComments();

        assertThat(storedComments).containsExactlyInAnyOrderElementsOf(comments);
    }
}
