package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenreRepository {

    Genre save(Genre genre);

    List<Genre> genres();

    List<Genre> genres(Iterable<UUID> ids);

    Optional<Genre> genre(UUID id);
}
