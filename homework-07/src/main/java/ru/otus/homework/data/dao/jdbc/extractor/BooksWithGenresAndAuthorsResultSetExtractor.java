package ru.otus.homework.data.dao.jdbc.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Book;
import ru.otus.homework.data.object.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BooksWithGenresAndAuthorsResultSetExtractor implements ResultSetExtractor<List<Book>> {

    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, Book> books = new TreeMap<>();
        Map<UUID, Genre> genres = new HashMap<>();
        Map<UUID, Set<UUID>> booksGenres = new HashMap<>();
        Map<UUID, Author> authors = new HashMap<>();
        Map<UUID, Set<UUID>> booksAuthors = new HashMap<>();

        while (rs.next()) {
            UUID bookId = uuid(rs, Column.BOOK_ID).get();
            Book book = books.get(bookId);
            if (book == null) {
                String bookTitle = string(rs, Column.BOOK_TITLE);
                book = new Book(bookId, bookTitle);
                books.put(book.getId(), book);

                booksGenres.put(book.getId(), new HashSet<>());
                booksAuthors.put(book.getId(), new HashSet<>());
            }

            Optional<UUID> genreIdWrapper = uuid(rs, Column.GENRE_ID);
            if (genreIdWrapper.isPresent()) {
                UUID genreId = genreIdWrapper.get();
                Set<UUID> bookGenres = booksGenres.get(bookId);
                if (!bookGenres.contains(genreId)) {
                    Genre genre = genres.computeIfAbsent(genreId, (id) -> genre(id, rs));
                    book.getGenres().add(genre);
                    bookGenres.add(genre.getId());
                }
            }

            Optional<UUID> authorIdWrapper = uuid(rs, Column.AUTHOR_ID);
            if (authorIdWrapper.isPresent()) {
                UUID authorId = authorIdWrapper.get();
                Set<UUID> bookAuthors = booksAuthors.get(bookId);
                if (!bookAuthors.contains(authorId)) {
                    Author author = authors.computeIfAbsent(authorId, (id) -> author(id, rs));
                    book.getAuthors().add(author);
                    bookAuthors.add(author.getId());
                }
            }
        }

        return new ArrayList<>(books.values());
    }

    private Genre genre(UUID id, ResultSet rs) {
        try {
            String genreName = string(rs, Column.GENRE_NAME);
            return new Genre(id, genreName);
        } catch (SQLException e) {
            throw new DataRetrievalFailureException("Unable to read genre data", e);
        }
    }

    private Author author(UUID id, ResultSet rs) {
        try {
            String authorName = string(rs, Column.AUTHOR_NAME);
            return new Author(id, authorName);
        } catch (SQLException e) {
            throw new DataRetrievalFailureException("Unable to read author data", e);
        }
    }

    private Optional<UUID> uuid(ResultSet rs, Column column) throws SQLException {
        String rawUuid = rs.getString(column.toString());
        if (rawUuid == null) {
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(rawUuid));
    }

    private String string(ResultSet rs, Column column) throws SQLException {
        return rs.getString(column.toString());
    }
}
