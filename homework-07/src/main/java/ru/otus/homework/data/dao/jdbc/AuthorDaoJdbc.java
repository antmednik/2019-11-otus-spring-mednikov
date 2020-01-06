package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.AuthorDao;
import ru.otus.homework.data.dao.jdbc.mapper.AuthorRowMapper;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private static final RowMapper<Author> AUTHOR_ROW_MAPPER = new AuthorRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void save(Author author) {
        jdbc.update("INSERT INTO author (id, name) VALUES(:id, :name)",
                Map.of("id", author.getId(), "name", author.getName()));
    }

    @Override
    public List<Author> authors() {
        return jdbc.query("select id, name from author", AUTHOR_ROW_MAPPER);
    }

    @Override
    public Optional<Author> authorById(UUID id) {
        try {
            Author author = jdbc.queryForObject("select id, name from author where id = :id",
                    Map.of("id", id), AUTHOR_ROW_MAPPER);
            return Optional.of(author);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
}
