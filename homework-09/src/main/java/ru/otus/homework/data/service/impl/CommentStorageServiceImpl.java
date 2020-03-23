package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.CommentRepository;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Comment;
import ru.otus.homework.data.service.CommentStorageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentStorageServiceImpl implements CommentStorageService {

    private final CommentRepository commentRepository;

    @Override
    public Comment save(String text, Book book) {
        return commentRepository.save(new Comment(UUID.randomUUID(), text, book));
    }
}
