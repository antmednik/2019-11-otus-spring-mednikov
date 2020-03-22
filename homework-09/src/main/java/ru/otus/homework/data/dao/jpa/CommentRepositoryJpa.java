package ru.otus.homework.data.dao.jpa;

import org.springframework.stereotype.Repository;
import ru.otus.homework.data.dao.CommentRepository;
import ru.otus.homework.data.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        }

        return em.merge(comment);
    }

    @Override
    public List<Comment> commentsByBook(UUID bookId) {
        var query = em.createQuery("select e from Comment e where e.book.id = :id",
                Comment.class);
        query.setParameter("id", bookId);
        return query.getResultList();
    }
}
