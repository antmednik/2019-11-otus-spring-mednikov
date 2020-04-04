package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.entity.mongo.Comment;
import ru.otus.homework.data.entity.mongo.Genre;

@Repository
public interface GenreRepository extends MongoRepository<Genre, ObjectId> {
}
