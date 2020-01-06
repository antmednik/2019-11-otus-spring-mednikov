package ru.otus.homework.data.dao.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.data.object.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book(UUID.fromString(rs.getString("book_id")));
        book.setTitle(rs.getString("book_title"));
        return book;
    }
}
