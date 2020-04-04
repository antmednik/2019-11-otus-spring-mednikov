package ru.otus.homework.data.service;

import ru.otus.homework.data.entity.mongo.Book;

import java.util.List;
import java.util.Optional;

public interface BookStorageService {

    Book save(String title, List<String> authorsIds, List<String> genresIds,
              List<String> comments);

    List<Book> books();

    Optional<Book> bookById(String bookId);

    boolean deleteBookById(String bookId);

    boolean update(String bookId, String newTitle,
                   List<String> authorsIds, List<String> genresIds);
}
