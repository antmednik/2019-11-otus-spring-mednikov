package ru.otus.homework.data.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
public class Genre {

    @Id
    private ObjectId id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
