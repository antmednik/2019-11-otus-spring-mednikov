package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.BookAuthorDao;
import ru.otus.homework.data.dao.jdbc.mapper.AuthorRowMapper;
import ru.otus.homework.data.object.Author;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookAuthorDaoJdbc implements BookAuthorDao {

    private static final RowMapper<Author> AUTHOR_ROW_MAPPER = new AuthorRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> authorsOfBook(UUID bookId) {
        return jdbc.query("select " +
                        "a.id as id, a.name as name " +
                "from book_author as ba " +
                "left join author as a on ba.author_id = a.id " +
                "where ba.book_id = :book_id",
                Map.of("book_id", bookId),
                AUTHOR_ROW_MAPPER);
    }

    @Override
    public void saveBookAuthorConnection(UUID bookId, UUID authorId) {
        jdbc.update("INSERT INTO public.book_author (book_id, author_id) VALUES " +
                "(:book_id, :author_id)",
                Map.of("book_id", bookId, "author_id", authorId));
    }

    @Override
    public void deleteBookAuthorConnection(UUID bookId) {
        jdbc.update(
                "delete from book_author where book_id = :book_id",
                Map.of("book_id", bookId));
    }

    @Override
    public void deleteBookAuthorConnection(UUID bookId, UUID authorId) {
        jdbc.update(
                "delete from book_author where book_id = :book_id and author_id = :author_id",
                Map.of("book_id", bookId, "author_id", authorId));
    }
}
