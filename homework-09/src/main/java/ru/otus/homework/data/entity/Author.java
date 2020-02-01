package ru.otus.homework.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "author")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Author {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;
}
