package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({CommentRepositoryJpa.class, BookRepositoryJpa.class})
public class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Test
    public void whenSearchForNonExistingCommentsThenNotFound(){
        List<Comment> comments = commentRepositoryJpa.commentsByBook(UUID.randomUUID());
        assertThat(comments).isEmpty();
    }

    @Test
    public void whenCommentSavedThenLoadedById() {
        final UUID commentId = UUID.randomUUID();
        final UUID bookId = UUID.randomUUID();
        final String bookTitle = UUID.randomUUID().toString();

        var savedBook = bookRepositoryJpa.save(new Book(bookId, bookTitle));
        assertThat(savedBook).isNotNull();

        var comment = new Comment(commentId, UUID.randomUUID().toString(), savedBook);
        var savedComment = commentRepositoryJpa.save(comment);
        assertThat(savedComment).isNotNull();

        List<Comment> storedComments = commentRepositoryJpa.commentsByBook(bookId);

        assertThat(storedComments).hasSize(1);
        assertThat(storedComments.get(0)).usingRecursiveComparison().isEqualTo(comment);
    }

    @Test
    public void whenThreeCommentsSavedThenThreeLoadedFromDao() {
        final UUID bookId = UUID.randomUUID();
        final String bookTitle = UUID.randomUUID().toString();

        var savedBook = bookRepositoryJpa.save(new Book(bookId, bookTitle));
        assertThat(savedBook).isNotNull();

        List<Comment> comments = List.of(
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook),
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook),
                new Comment(UUID.randomUUID(), UUID.randomUUID().toString(), savedBook));

        for (var comment : comments){
            commentRepositoryJpa.save(comment);
        }

        List<Comment> storedComments = commentRepositoryJpa.commentsByBook(bookId);

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
        assertThatThrownBy(() -> commentRepositoryJpa.save(null))
                .isInstanceOf(NullPointerException.class);
    }
}
