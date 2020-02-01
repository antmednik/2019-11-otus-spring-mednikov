package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.BookGenreDao;
import ru.otus.homework.data.dao.jdbc.mapper.GenreRowMapper;
import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookGenreDaoJdbc implements BookGenreDao {

    private static final RowMapper<Genre> GENRE_ROW_MAPPER = new GenreRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> genresOfBook(UUID bookId) {
        return jdbc.query("select " +
                        "g.id as id, g.name as name " +
                        "from book_genre as bg " +
                        "left join genre as g on bg.genre_id = g.id " +
                        "where bg.book_id = :book_id",
                Map.of("book_id", bookId),
                GENRE_ROW_MAPPER);
    }

    @Override
    public void saveBookGenreConnection(UUID bookId, UUID genreId) {
        jdbc.update(
                "INSERT INTO public.book_genre (book_id, genre_id) VALUES " +
                        "(:book_id, :genre_id)",
                Map.of("book_id", bookId, "genre_id", genreId));
    }

    @Override
    public void deleteBookGenreConnection(UUID bookId) {
        jdbc.update(
        "delete from book_genre where book_id = :book_id",
            Map.of("book_id", bookId));
    }

    @Override
    public void deleteBookGenreConnection(UUID bookId, UUID genreId) {
        jdbc.update(
                "delete from book_genre where book_id = :book_id and genre_id = :genre_id",
                Map.of("book_id", bookId, "genre_id", genreId));
    }

}
