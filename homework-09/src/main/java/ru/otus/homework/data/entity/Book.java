package ru.otus.homework.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @OneToMany
    @JoinTable
    private List<Author> authors;

    @OneToMany
    @JoinTable
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
