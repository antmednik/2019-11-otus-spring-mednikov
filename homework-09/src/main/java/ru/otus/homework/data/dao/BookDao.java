package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookDao {
    Book save(Book book);

    Optional<Book> bookById(UUID id);

    List<Book> books();

    boolean updateTitle(UUID bookId, String newTitle);

    boolean deleteById(UUID id);
}
