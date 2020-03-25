package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository {

    Author save(Author author);

    List<Author> authors();

    List<Author> authors(Iterable<UUID> ids);

    Optional<Author> author(UUID id);
}
