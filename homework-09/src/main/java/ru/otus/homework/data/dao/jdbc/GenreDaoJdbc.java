package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
//import ru.otus.homework.data.dao.GenreDao;
import ru.otus.homework.data.dao.GenreDao;
import ru.otus.homework.data.dao.jdbc.repository.GenreRepository;
import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final GenreRepository genreRepository;

    @Override
    public Genre save(Genre genre) {
        genreRepository.save(genre);

        return genreById(genre.getId()).orElseThrow(() ->
                new DataRetrievalFailureException(
                        String.format("Unable to load genre by id '%s'", genre.getId())));
    }

    @Override
    public List<Genre> genres() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> genreById(UUID genreId) {
        try {
            return genreRepository.findById(genreId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
}
