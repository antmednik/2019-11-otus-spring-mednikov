package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.entity.mongo.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void whenSearchForNonExistingGenreThenNotFound(){
        Optional<Genre> genre = genreRepository.findById(new ObjectId());
        assertThat(genre).isEmpty();
    }

    @Test
    public void whenGenreSavedThenLoadedById() {
        Genre genre = new Genre(UUID.randomUUID().toString());
        var savedGenre = genreRepository.save(genre);

        Optional<Genre> storedGenre = genreRepository.findById(savedGenre.getId());

        assertThat(storedGenre).isNotEmpty();
        assertThat(storedGenre.get()).usingRecursiveComparison().isEqualTo(genre);
    }

    @Test
    public void whenNoGenresThenGetAllReturnsEmptyList() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(0);
    }

    @Test
    public void whenThreeGenresSavedThenThreeLoadedFromDao() {
        List<Genre> genres = List.of(
                new Genre(UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID().toString()));

        for (Genre genre : genres){
            genreRepository.save(genre);
        }

        List<Genre> storedGenres = genreRepository.findAll();

        assertThat(storedGenres).hasSize(genres.size());
        for (Genre storedGenre : storedGenres) {
            Optional<Genre> genre = genres.stream()
                    .filter(g -> g.getId().equals(storedGenre.getId()))
                    .findFirst();

            assertThat(genre).isNotEmpty();
            assertThat(genre.get()).usingRecursiveComparison().isEqualTo(storedGenre);
        }
    }

    @Test
    public void whenSavedNullObjThenNPEThrown() {
        assertThatThrownBy(() -> genreRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
