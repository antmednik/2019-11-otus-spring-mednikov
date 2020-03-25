package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.GenreRepository;
import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreStorageServiceImpl implements ru.otus.homework.data.service.GenreStorageService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> genres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre save(String name) {
        return genreRepository.save(new Genre(UUID.randomUUID(), name));
    }
}
