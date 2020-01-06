package ru.otus.homework.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.data.dao.jdbc.AuthorDaoJdbc;
import ru.otus.homework.data.dao.jdbc.GenreDaoJdbc;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.object.Genre;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorStorageService {

    private final AuthorDaoJdbc authorDaoJdbc;

    public List<Author> authors() {
        return authorDaoJdbc.authors();
    }

    public void save(String name) {
        Author author = new Author(UUID.randomUUID());
        author.setName(name);
        authorDaoJdbc.save(author);
    }
}
