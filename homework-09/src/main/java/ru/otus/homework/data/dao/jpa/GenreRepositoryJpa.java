package ru.otus.homework.data.dao.jpa;

import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.GenreRepository;
import ru.otus.homework.data.entity.Author;
import ru.otus.homework.data.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            em.persist(genre);
            return genre;
        }

        return em.merge(genre);
    }

    @Override
    public List<Genre> genres() {
        return em.createQuery("select e from Genre e", Genre.class)
                .getResultList();
    }

    @Override
    public List<Genre> genres(Iterable<UUID> ids) {
        var query = em.createQuery("select e from Genre e where e.id in :ids",
                Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> genre(UUID id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
}
