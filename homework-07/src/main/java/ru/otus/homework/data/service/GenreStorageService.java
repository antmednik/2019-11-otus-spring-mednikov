package ru.otus.homework.data.service;

import ru.otus.homework.data.object.Genre;

import java.util.List;

public interface GenreStorageService {
    List<Genre> genres();

    Genre save(String name);
}
