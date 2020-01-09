package ru.otus.homework.data.service.impl;

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
public class AuthorStorageServiceImpl implements ru.otus.homework.data.service.AuthorStorageService {

    private final AuthorDaoJdbc authorDaoJdbc;

    @Override
    public List<Author> authors() {
        return authorDaoJdbc.authors();
    }

    @Override
    public Author save(String name) {
        Author author = new Author(UUID.randomUUID());
        author.setName(name);
        return authorDaoJdbc.save(author);
    }
}
