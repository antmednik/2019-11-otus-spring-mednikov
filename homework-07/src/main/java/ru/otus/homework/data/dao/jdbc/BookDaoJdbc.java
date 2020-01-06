package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.BookDao;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Book;
import ru.otus.homework.data.object.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private static final RowMapper<Book> BOOK_ROW_MAPPER = new BookRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Book save(Book book) {
        jdbc.update("insert into book (id, title) values" +
                "(':book_id', ':title')",
                Map.of("book_id", book.getId(),
                        "title", book.getTitle()));

        return bookById(book.getId()).orElseThrow(() ->
                new DataRetrievalFailureException(
                        String.format("Unable to load book by id '%s'", book.getId())));
    }

    @Override
    public Optional<Book> bookById(UUID id) {
        Book book = jdbc.queryForObject("select " +
                        "b.id as book_id, b.title as book_title " +
                        "from book as b " +
                        "where b.id = :book_id",
                Map.of("book_id", id),
                BOOK_ROW_MAPPER);
        /*Book book = jdbc.queryForObject("select " +
                        "b.id as book_id, b.title as book_title, " +
                        "a.id as author_id, a.name as author_name, " +
                        "g.id as genre_id, g.name as genre_name " +Statefull
                        "from book as b " +
                        "left join book_author as ba on ba.book_id=b.id " +
                        "left join author as a on a.id = ba.author_id " +
                        "left join book_genre as bg on b.id=bg.book_id " +
                        "left join genre as g on g.id=bg.genre_id " +
                        "where b.id = :book_id",
                new MapSqlParameterSource(Map.of("book_id", id)),
                new StatefullBookRowMapper());*/

        if (book == null) {
            return Optional.empty();
        }

        return Optional.of(book);
    }

    @Override
    public List<Book> books() {
        return jdbc.query("select " +
                        "b.id as book_id, b.title as book_title " +
                        "from book as b ",
                BOOK_ROW_MAPPER);
        /*return jdbc.query("select " +
                "b.id as book_id, b.title as book_title, " +
                "a.id as author_id, a.name as author_name, " +
                "g.id as genre_id, g.name as genre_name " +
                "from book as b " +
                "left join book_author as ba on ba.book_id=b.id " +
                "left join author as a on a.id = ba.author_id " +
                "left join book_genre as bg on b.id=bg.book_id " +
                "left join genre as g on g.id=bg.genre_id " +
                "order by book_id", new StatefullBookRowMapper());*/
    }

    @Override
    public void updateTitle(UUID bookId, String newTitle) {
        jdbc.update("update book set title = :new_title where id = :id",
                Map.of("id", bookId, "new_title", newTitle));
    }

    @Override
    public void deleteById(UUID id) {
        jdbc.update("delete from book where id = :id",
                Map.of("id", id));
    }

    static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book(UUID.fromString(rs.getString("book_id")));
            book.setTitle(rs.getString("book_title"));
            return book;
        }
    }

    static class StatefullBookRowMapper implements RowMapper<Book> {

        private Book book;
        private Map<UUID, Author> authors = new HashMap<>();
        private Map<UUID, Genre> genres = new HashMap<>();

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            boolean nextBook = false;
            Book currentBook = null;
            while (!nextBook && rs.next()) {
                UUID bookId = UUID.fromString(rs.getString("book_id"));

                if (book == null || !book.getId().equals(bookId)) {
                    if (book != null && !book.getId().equals(bookId)) {
                        nextBook = true;

                        currentBook = book;
                        currentBook.setAuthors(new ArrayList(authors.values()));
                        currentBook.setGenres(new ArrayList(genres.values()));

                        authors.clear();
                        genres.clear();
                    }

                    book = new Book(bookId);
                    book.setTitle(rs.getString("book_title"));
                }

                UUID authorId = UUID.fromString(rs.getString("author_id"));
                if (!authors.containsKey(authorId)) {
                    Author author = new Author(authorId);
                    author.setName(rs.getString("author_name"));
                    authors.put(authorId, author);
                }

                UUID genreId = UUID.fromString(rs.getString("genre_id"));
                if (!genres.containsKey(genreId)) {
                    Genre genre = new Genre(genreId, rs.getString("genre_name"));
                    genres.put(genreId, genre);
                }
            }

            if (nextBook) {
                return currentBook;
            }

            if (book != null) {
                book.setAuthors(new ArrayList(authors.values()));
                book.setGenres(new ArrayList(genres.values()));
            }

            return book;
        }
    }
}
