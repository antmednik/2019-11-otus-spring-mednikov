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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }

        return em.merge(book);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Book> bookById(UUID id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> books() {
        return em.createQuery("select e from Book e", Book.class)
                .getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateTitle(UUID bookId, String newTitle) {
        var query = em.createQuery(
                "update Book set title = :title where id = :id");
        query.setParameter("id", bookId);
        query.setParameter("title", newTitle);
        return query.executeUpdate() == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteById(UUID id) {
        var query = em.createQuery(
                "delete Book where id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() == 1;

        //em.createQuery()
    }
}
