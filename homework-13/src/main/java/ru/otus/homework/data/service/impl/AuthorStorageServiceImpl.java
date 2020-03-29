package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.mongo.AuthorRepository;
import ru.otus.homework.data.entity.mongo.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorStorageServiceImpl implements ru.otus.homework.data.service.AuthorStorageService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> authors() {
        return authorRepository.findAll();
    }

    @Override
    public Author save(String name) {
        Author author = new Author(name);
        return authorRepository.save(author);
    }
}
