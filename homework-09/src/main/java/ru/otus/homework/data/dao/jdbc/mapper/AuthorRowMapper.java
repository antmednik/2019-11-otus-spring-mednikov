package ru.otus.homework.data.dao.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author(UUID.fromString(rs.getString("id")));
        author.setName(rs.getString("name"));
        return author;
    }
}
