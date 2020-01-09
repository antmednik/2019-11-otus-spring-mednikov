package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.data.dao.jdbc.GenreDaoJdbc;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreStorageServiceImpl implements ru.otus.homework.data.service.GenreStorageService {

    private final GenreDaoJdbc genreDaoJdbc;

    @Override
    public List<Genre> genres() {
        return genreDaoJdbc.genres();
    }

    @Override
    public Genre save(String name) {
        return genreDaoJdbc.save(new Genre(UUID.randomUUID(), name));
    }
}
