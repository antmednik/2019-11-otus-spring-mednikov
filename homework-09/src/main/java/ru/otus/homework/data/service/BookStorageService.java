package ru.otus.homework.data.service;

import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookStorageService {

    Book save(String title, List<UUID> authorsIds, List<UUID> genresIds,
              List<String> comments);

    List<Book> books();

    Optional<Book> bookById(UUID bookId);

    boolean deleteBookById(UUID bookId);

    boolean update(UUID bookId, String newTitle, List<UUID> authorsIds, List<UUID> genresIds);
}
