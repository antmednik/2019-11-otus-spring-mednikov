package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.AuthorRepository;
import ru.otus.homework.data.entity.Author;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorStorageServiceImpl implements ru.otus.homework.data.service.AuthorStorageService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> authors() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author save(String name) {
        Author author = new Author(UUID.randomUUID(), name);
        return authorRepository.save(author);
    }
}
