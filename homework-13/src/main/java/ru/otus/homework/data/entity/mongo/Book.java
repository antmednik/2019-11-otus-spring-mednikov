package ru.otus.homework.data.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
@Getter
@Setter
@ToString
public class Book {

    @Id
    private ObjectId id;

    private String title;

    @DBRef
    private List<Author> authors;

    @DBRef
    private List<Genre> genres;

    private List<Comment> comments;

    public Book(String title) {
        this.title = title;

        authors = new ArrayList<>();
        genres = new ArrayList<>();
        comments = new ArrayList<>();
    }
}
