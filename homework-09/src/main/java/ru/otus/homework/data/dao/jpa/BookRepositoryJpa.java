package ru.otus.homework.data.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.BookRepository;
import ru.otus.homework.data.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }

        return em.merge(book);
    }

    @Override
    public Optional<Book> bookById(UUID id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> books() {
        return em.createQuery("select e from Book e", Book.class)
                .getResultList();
    }

    @Override
    public boolean deleteById(UUID id) {
        var book = em.find(Book.class, id);
        if (book == null) {
            return false;
        }

        em.remove(book);
        return true;
    }
}
