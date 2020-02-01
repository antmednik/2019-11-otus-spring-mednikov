package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.AuthorDao;
import ru.otus.homework.data.dao.jdbc.mapper.AuthorRowMapper;
import ru.otus.homework.data.dao.jdbc.repository.AuthorRepository;
import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final AuthorRepository repository;

    @Override
    public Author save(Author author) {
        return repository.save(author);
    }

    @Override
    public List<Author> authors() {
        return repository.findAll();
    }

    @Override
    public Optional<Author> authorById(UUID id) {
        return repository.findById(id);
    }
}
