package ru.otus.homework.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "genre")
@RequiredArgsConstructor
@Getter
@ToString
public class Genre {

    @Id
    @Column(name = "id")
    private UUID id;

    @Id
    @Column(name = "name")
    private String name;
}
