package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.UUID;

public interface BookAuthorDao {

    List<Author> authorsOfBook(UUID bookId);

    void saveBookAuthorConnection(UUID bookId, UUID authorId);

    void deleteBookAuthorConnection(UUID bookId);

    void deleteBookAuthorConnection(UUID bookId, UUID authorId);
}
