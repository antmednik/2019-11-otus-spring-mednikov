package ru.otus.homework.data.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Book {

    private final UUID id;

    private String title;

    private List<Author> authors;

    private List<Genre> genres;

    public Book(UUID id){
        this(id, null);
    }

    public Book(UUID id, String title) {
        this.id = id;
        this.title = title;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }
}
