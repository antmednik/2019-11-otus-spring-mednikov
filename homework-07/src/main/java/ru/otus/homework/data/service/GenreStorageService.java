package ru.otus.homework.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.data.dao.jdbc.GenreDaoJdbc;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreStorageService {

    private final GenreDaoJdbc genreDaoJdbc;

    public List<Genre> genres() {
        return genreDaoJdbc.genres();
    }

    public void save(String name) {
        genreDaoJdbc.save(new Genre(UUID.randomUUID(), name));
    }
}
