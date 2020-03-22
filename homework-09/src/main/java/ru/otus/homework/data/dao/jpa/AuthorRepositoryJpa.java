package ru.otus.homework.data.dao.jpa;

import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.AuthorRepository;
import ru.otus.homework.data.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        }

        return em.merge(author);
    }

    @Override
    public List<Author> authors() {
        return em.createQuery("select e from Author e", Author.class)
                .getResultList();
    }

    @Override
    public List<Author> authors(Iterable<UUID> ids) {
        var query = em.createQuery("select e from Author e where e.id in :ids",
                Author.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Optional<Author> author(UUID id) {
        var query = em.createQuery("select e from Author e where e.id = :id",
                Author.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
