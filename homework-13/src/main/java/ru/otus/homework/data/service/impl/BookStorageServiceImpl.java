package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.data.dao.mongo.AuthorRepository;
import ru.otus.homework.data.dao.mongo.BookRepository;
import ru.otus.homework.data.dao.mongo.GenreRepository;
import ru.otus.homework.data.entity.mongo.Book;
import ru.otus.homework.data.entity.mongo.Comment;
import ru.otus.homework.data.service.BookStorageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BookStorageServiceImpl implements BookStorageService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Book save(String title, List<String> authorsIds,
                     List<String> genresIds, List<String> comments) {

        Book book = new Book(title);

        var authors = authorRepository.findAllById(convertIds(authorsIds));
        var genres = genreRepository.findAllById(convertIds(genresIds));

        book.setGenres(IterableUtils.toList(genres));
        book.setAuthors(IterableUtils.toList(authors));
        book.setComments(comments.stream()
                .map(Comment::new)
                .collect(toList()));

        return bookRepository.save(book);
    }

    @Override
    public List<Book> books() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> bookById(String bookId) {
        return bookRepository.findById(new ObjectId(bookId));
    }

    @Override
    public boolean deleteBookById(String bookId) {
        bookRepository.deleteById(new ObjectId(bookId));
        return true;
    }

    @Override
    @Transactional
    public boolean update(String bookId, String newTitle,
                          List<String> authorsIds, List<String> genresIds) {
        Book book = bookRepository.findById(new ObjectId(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found."));
        book.setTitle(newTitle);

        var authors = authorRepository.findAllById(convertIds(authorsIds));
        var genres = genreRepository.findAllById(convertIds(genresIds));

        book.setGenres(IterableUtils.toList(genres));
        book.setAuthors(IterableUtils.toList(authors));

        bookRepository.save(book);

        return true;
    }

    private Iterable<ObjectId> convertIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toSet());
    }

    private <T> List<T> asList(Iterable<T> iters) {
        return StreamSupport
                .stream(iters.spliterator(), false)
                .collect(toList());
    }
}
