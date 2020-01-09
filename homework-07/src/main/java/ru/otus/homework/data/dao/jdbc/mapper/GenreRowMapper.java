package ru.otus.homework.data.dao.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.data.object.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"));
    }
}
