package ru.otus.homework.data.dao.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.dao.GenreRepository;
import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void whenSearchForNonExistingGenreThenNotFound(){
        Optional<Genre> genre = genreRepository.findById(UUID.randomUUID());
        assertThat(genre).isEmpty();
    }

    @Test
    public void whenGenreSavedThenLoadedById() {
        final UUID genreId = UUID.randomUUID();
        Genre genre = new Genre(genreId, UUID.randomUUID().toString());
        genreRepository.save(genre);

        Optional<Genre> storedGenre = genreRepository.findById(genreId);

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
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()));

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
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}
