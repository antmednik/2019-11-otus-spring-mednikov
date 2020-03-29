package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.entity.mongo.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, ObjectId> {
}
