package ru.otus.homework.data.dao;

import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.UUID;

public interface BookGenreDao {

    List<Genre> genresOfBook(UUID bookId);

    void saveBookGenreConnection(UUID bookId, UUID genreId);

    void deleteBookGenreConnection(UUID bookId);

    void deleteBookGenreConnection(UUID bookId, UUID genreId);
}
