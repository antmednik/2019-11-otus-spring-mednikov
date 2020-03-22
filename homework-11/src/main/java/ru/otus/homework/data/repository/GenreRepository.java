package ru.otus.homework.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.data.entity.Author;
import ru.otus.homework.data.entity.Genre;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    List<Genre> findAllByIdIn(Iterable<UUID> ids);
}
