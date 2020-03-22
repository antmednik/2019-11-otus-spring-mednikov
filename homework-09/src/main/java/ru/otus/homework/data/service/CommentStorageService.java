package ru.otus.homework.data.service;

import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.entity.Comment;

import java.util.List;

public interface CommentStorageService {
    List<Comment> commentsByBook(Book book);

    Comment save(String text, Book book);
}
