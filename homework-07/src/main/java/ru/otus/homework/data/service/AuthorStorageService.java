package ru.otus.homework.data.service;

import ru.otus.homework.data.object.Author;

import java.util.List;

public interface AuthorStorageService {
    List<Author> authors();

    Author save(String name);
}
