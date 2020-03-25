package ru.otus.homework.data.service;

import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Comment;

import java.util.Set;
import java.util.UUID;

public interface CommentStorageService {
    Set<Comment> commentsByBook(UUID bookId);

    Comment save(String text, Book book);
}
