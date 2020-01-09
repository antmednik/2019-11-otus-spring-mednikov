package ru.otus.homework.data.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @Test
    public void whenSearchForNonExistingGenreThenNotFound(){
        Optional<Genre> genre = genreDaoJdbc.genreById(UUID.randomUUID());
        assertThat(genre).isEmpty();
    }

    @Test
    public void whenGenreSavedThenLoadedById() {
        final UUID genreId = UUID.randomUUID();
        Genre genre = new Genre(genreId, UUID.randomUUID().toString());
        genreDaoJdbc.save(genre);

        Optional<Genre> storedGenre = genreDaoJdbc.genreById(genreId);

        assertThat(storedGenre).isNotEmpty();
        assertThat(storedGenre.get()).usingRecursiveComparison().isEqualTo(genre);
    }

    @Test
    public void whenNoGenresThenGetAllReturnsEmptyList() {
        List<Genre> genres = genreDaoJdbc.genres();
        assertThat(genres).hasSize(0);
    }

    @Test
    public void whenThreeGenresSavedThenThreeLoadedFromDao() {
        List<Genre> genres = List.of(
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()),
                new Genre(UUID.randomUUID(), UUID.randomUUID().toString()));

        for (Genre genre : genres){
            genreDaoJdbc.save(genre);
        }

        List<Genre> storedGenres = genreDaoJdbc.genres();

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
        assertThatThrownBy(() -> genreDaoJdbc.save(null))
                .isInstanceOf(NullPointerException.class);
    }
}
