package ru.otus.homework.data.service;

import java.util.List;

public interface AuthorStorageService {
    List<Author> authors();

    Author save(String name);
}
