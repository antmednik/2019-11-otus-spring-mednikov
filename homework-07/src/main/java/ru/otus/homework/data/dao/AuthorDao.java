package ru.otus.homework.data.dao;

import ru.otus.homework.data.object.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorDao {

    Author save(Author author);

    List<Author> authors();

    Optional<Author> authorById(UUID id);
}
