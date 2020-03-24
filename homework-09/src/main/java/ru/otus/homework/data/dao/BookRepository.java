package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> bookById(UUID id);

    List<Book> books();

    boolean deleteById(UUID id);
}
