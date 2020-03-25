package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.dao.AuthorRepository;
import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepositoryJpa;

    @Test
    public void whenSearchForNonExistingAuthorThenNotFound(){
        Optional<Author> author = authorRepositoryJpa.findById(UUID.randomUUID());
        assertThat(author).isEmpty();
    }

    @Test
    public void whenAuthorSavedThenLoadedById() {
        final UUID authorId = UUID.randomUUID();
        Author author = new Author(authorId, UUID.randomUUID().toString());
        authorRepositoryJpa.save(author);

        Optional<Author> storedAuthor = authorRepositoryJpa.findById(authorId);

        assertThat(storedAuthor).isNotEmpty();
        assertThat(storedAuthor.get()).usingRecursiveComparison().isEqualTo(author);
    }

    @Test
    public void whenNoAuthorsThenGetAllReturnsEmptyList() {
        List<Author> authors = authorRepositoryJpa.findAll();
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

        List<Author> storedAuthors = authorRepositoryJpa.findAll();

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
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}
