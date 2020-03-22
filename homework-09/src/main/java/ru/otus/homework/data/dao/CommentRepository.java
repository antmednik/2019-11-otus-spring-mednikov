package ru.otus.homework.data.dao;

import ru.otus.homework.data.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository {

    Comment save(Comment comment);

    List<Comment> commentsByBook(UUID bookId);
}
