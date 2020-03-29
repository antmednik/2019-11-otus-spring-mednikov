package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.mongo.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenSearchForNonExistingAuthorThenNotFound(){
        Optional<Author> author = authorRepository.findById(new ObjectId());
        assertThat(author).isEmpty();
    }

    @Test
    public void whenAuthorSavedThenLoadedById() {
        Author author = new Author(UUID.randomUUID().toString());
        var savedAuthor = authorRepository.save(author);

        Optional<Author> storedAuthor = authorRepository.findById(savedAuthor.getId());

        assertThat(storedAuthor).isNotEmpty();
        assertThat(storedAuthor.get()).usingRecursiveComparison().isEqualTo(author);
    }

    @Test
    public void whenNoAuthorsThenGetAllReturnsEmptyList() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(0);
    }

    @Test
    public void whenThreeAuthorsSavedThenThreeLoadedFromDao() {
        List<Author> authors = List.of(
                new Author(UUID.randomUUID().toString()),
                new Author(UUID.randomUUID().toString()),
                new Author(UUID.randomUUID().toString()));

        for (Author author : authors){
            authorRepository.save(author);
        }

        List<Author> storedAuthors = authorRepository.findAll();

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
        assertThatThrownBy(() -> authorRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
