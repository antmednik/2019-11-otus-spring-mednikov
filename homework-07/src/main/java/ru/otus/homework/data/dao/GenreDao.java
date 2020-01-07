package ru.otus.homework.data.dao;

import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenreDao {

    Genre save(Genre genre);

    List<Genre> genres();

    Optional<Genre> genreById(UUID genreId);
}
