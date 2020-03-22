package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Test
    public void whenSearchForNonExistingAuthorThenNotFound(){
        Optional<Author> author = authorRepositoryJpa.author(UUID.randomUUID());
        assertThat(author).isEmpty();
    }

    @Test
    public void whenAuthorSavedThenLoadedById() {
        final UUID authorId = UUID.randomUUID();
        Author author = new Author(authorId, UUID.randomUUID().toString());
        authorRepositoryJpa.save(author);

        Optional<Author> storedAuthor = authorRepositoryJpa.author(authorId);

        assertThat(storedAuthor).isNotEmpty();
        assertThat(storedAuthor.get()).usingRecursiveComparison().isEqualTo(author);
    }

    @Test
    public void whenNoAuthorsThenGetAllReturnsEmptyList() {
        List<Author> authors = authorRepositoryJpa.authors();
        assertThat(authors).hasSize(0);
    }

    @Test
    public void whenThreeAuthorsSavedThenThreeLoadedFromDao() {
        List<Author> authors = List.of(
                new Author(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Author(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Author(UUID.randomUUID(), UUID.randomUUID().toString()));

        for (Author author : authors){
            authorRepositoryJpa.save(author);
        }

        List<Author> storedAuthors = authorRepositoryJpa.authors();

        assertThat(storedAuthors).hasSize(authors.size());
        for (Author storedAuthor : storedAuthors) {
            Optional<Author> author = authors.stream()
                    .filter(g -> g.getId().equals(storedAuthor.getId()))
                    .findFirst();

            assertThat(author).isNotEmpty();
            assertThat(author.get()).usingRecursiveComparison().isEqualTo(storedAuthor);
        }
    }

    @Test
    public void whenSavedNullObjThenNPEThrown() {
        assertThatThrownBy(() -> authorRepositoryJpa.save(null))
                .isInstanceOf(NullPointerException.class);
    }
}
