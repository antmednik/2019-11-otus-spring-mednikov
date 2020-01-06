package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.GenreDao;
import ru.otus.homework.data.dao.jdbc.mapper.GenreRowMapper;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private static final RowMapper<Genre> GENRE_ROW_MAPPER = new GenreRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void save(Genre genre) {
        jdbc.update("INSERT INTO genre (id, name) VALUES(:id, :name)",
                Map.of("id", genre.getId(), "name", genre.getName()));
    }

    @Override
    public List<Genre> genres() {
        return jdbc.query("select id, name from genre", GENRE_ROW_MAPPER);
    }

    @Override
    public Optional<Genre> genreById(UUID genreId) {
        try {
            Genre genre = jdbc.queryForObject("select id, name from genre where id = :id",
                    Map.of("id", genreId), GENRE_ROW_MAPPER);
            return Optional.of(genre);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
}
