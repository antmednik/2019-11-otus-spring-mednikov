package ru.otus.homework.data.dao.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.entity.mongo.Author;
import ru.otus.homework.data.entity.mongo.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {
}
