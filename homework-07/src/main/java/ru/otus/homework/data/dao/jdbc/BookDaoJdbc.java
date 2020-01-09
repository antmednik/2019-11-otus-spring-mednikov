package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.BookDao;
import ru.otus.homework.data.dao.jdbc.extractor.BooksWithGenresAndAuthorsResultSetExtractor;
import ru.otus.homework.data.dao.jdbc.extractor.Column;
import ru.otus.homework.data.object.Book;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private static final ResultSetExtractor<List<Book>> BOOK_LIST_RS_EXTRACTOR =
            new BooksWithGenresAndAuthorsResultSetExtractor();

    private static final String SELECT_BOOKS_WITH_AUTHORS_AND_GENRES = "select " +
            "b.id as " + Column.BOOK_ID +
            ",b.title as " + Column.BOOK_TITLE +
            ",a.id as " + Column.AUTHOR_ID +
            ",a.name as " + Column.AUTHOR_NAME +
            ",g.id as " + Column.GENRE_ID +
            ",g.name as " + Column.GENRE_NAME +
            " from book as b " +
            "left join book_genre as bg on b.id = bg.book_id " +
            "left join genre as g on bg.genre_id = g.id " +
            "left join book_author as ba on b.id = ba.book_id " +
            "left join author as a on ba.author_id = a.id";

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Book save(Book book) {
        jdbc.update("insert into book (id, title) values" +
                        "(:book_id, :title)",
                Map.of("book_id", book.getId(),
                        "title", book.getTitle()));

        return bookById(book.getId()).orElseThrow(() ->
                new DataRetrievalFailureException(
                        String.format("Unable to load book by id '%s'", book.getId())));
    }

    @Override
    public Optional<Book> bookById(UUID id) {
        List<Book> book = jdbc.query(SELECT_BOOKS_WITH_AUTHORS_AND_GENRES +
                        " where b.id = :book_id",
                Map.of("book_id", id),
                BOOK_LIST_RS_EXTRACTOR);
        if (Objects.requireNonNull(book).size() == 0) {
            return Optional.empty();
        }

        return Optional.of(book.get(0));
    }

    @Override
    public List<Book> books() {
        return jdbc.query(SELECT_BOOKS_WITH_AUTHORS_AND_GENRES,
                BOOK_LIST_RS_EXTRACTOR);
    }

    @Override
    public boolean updateTitle(UUID bookId, String newTitle) {
        int rowsAffected = jdbc.update(
                "update book set title = :new_title where id = :id",
                Map.of("id", bookId, "new_title", newTitle));
        return rowsAffected == 1;
    }

    @Override
    public boolean deleteById(UUID id) {
        int rowsAffected = jdbc.update("delete from book where id = :id",
                Map.of("id", id));
        return rowsAffected == 1;
    }
}
