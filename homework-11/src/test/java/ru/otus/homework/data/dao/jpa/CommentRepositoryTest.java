package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.dao.BookRepository;
import ru.otus.homework.data.dao.CommentRepository;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenCommentSavedThenLoadedById() {
        final UUID commentId = UUID.randomUUID();
        final UUID bookId = UUID.randomUUID();
        final String bookTitle = UUID.randomUUID().toString();

        var savedBook = bookRepository.save(new Book(bookId, bookTitle));
        assertThat(savedBook).isNotNull();

        var comment = new Comment(commentId, UUID.randomUUID().toString(), savedBook);

        commentRepository.save(comment);

        savedBook.getComments().add(comment);

        bookRepository.save(savedBook);

        var storedBook = bookRepository.findById(savedBook.getId())
                .orElseThrow();

        var storedComments = storedBook.getComments();

        assertThat(storedComments).hasSize(1);
        assertThat(storedComments.stream().findFirst().get())
                .usingRecursiveComparison().isEqualTo(comment);
    }

    @Test
    public void whenThreeCommentsSavedThenThreeLoadedFromDao() {
        final UUID bookId = UUID.randomUUID();
        final String bookTitle = UUID.randomUUID().toString();

        var savedBook = bookRepository.save(new Book(bookId, bookTitle));
        assertThat(savedBook).isNotNull();

        List<Comment> comments = List.of(
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook),
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook),
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook));

        for (var comment : comments){
            commentRepository.save(comment);
            savedBook.getComments().add(comment);
        }
        bookRepository.save(savedBook);

        var storedComments = bookRepository.findById(savedBook.getId())
                .orElseThrow().getComments();

        assertThat(storedComments).hasSize(comments.size());
        for (var storedComment : storedComments) {
            Optional<Comment> comment = comments.stream()
                    .filter(g -> g.getId().equals(storedComment.getId()))
                    .findFirst();

            assertThat(comment).isNotEmpty();
            assertThat(comment.get()).usingRecursiveComparison().isEqualTo(storedComment);
        }
    }

    @Test
    public void whenSavedNullObjThenNPEThrown() {
        assertThatThrownBy(() -> commentRepository.save(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}
