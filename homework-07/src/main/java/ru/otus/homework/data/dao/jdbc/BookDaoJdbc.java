package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.BookDao;
import ru.otus.homework.data.dao.jdbc.mapper.BookRowMapper;
import ru.otus.homework.data.object.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private static final RowMapper<Book> BOOK_ROW_MAPPER = new BookRowMapper();

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
        try {
            Book book = jdbc.queryForObject("select " +
                            "b.id as book_id, b.title as book_title " +
                            "from book as b " +
                            "where b.id = :book_id",
                    Map.of("book_id", id),
                    BOOK_ROW_MAPPER);
            return Optional.of(book);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> books() {
        return jdbc.query("select " +
                        "b.id as book_id, b.title as book_title " +
                        "from book as b ",
                BOOK_ROW_MAPPER);
    }

    @Override
    public boolean updateTitle(UUID bookId, String newTitle) {
        int rowsAffected = jdbc.update("update book set title = :new_title where id = :id",
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
