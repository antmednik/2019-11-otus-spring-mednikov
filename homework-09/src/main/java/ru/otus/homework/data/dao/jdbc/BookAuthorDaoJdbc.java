package ru.otus.homework.data.dao.jdbc;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.data.dao.BookAuthorDao;
import ru.otus.homework.data.dao.jdbc.repository.AuthorRepository;
import ru.otus.homework.data.dao.jdbc.repository.BookRepository;
import ru.otus.homework.data.entity.Author;
import ru.otus.homework.data.entity.Book;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class BookAuthorDaoJdbc implements BookAuthorDao {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> authorsOfBook(UUID bookId) {
        Book book = bookRepository.getOne(bookId);
        return book.getAuthors();
    }

    @Override
    public void saveBookAuthorConnection(UUID bookId, UUID authorId) {
        Book book = bookRepository.getOne(bookId);
        Author author = authorRepository.getOne(authorId);
        book.getAuthors().add(author);
        bookRepository.save(book);
    }

    @Override
    public void deleteBookAuthorConnection(UUID bookId) {
        Book book = bookRepository.getOne(bookId);
        book.getAuthors().clear();
        bookRepository.save(book);
    }

    @Override
    public void deleteBookAuthorConnection(UUID bookId, UUID authorId) {
        Book book = bookRepository.getOne(bookId);
        for(int index = 0; index < book.getAuthors().size(); index++) {
            if (book.getAuthors().get(index).getId().equals(authorId)) {
                book.getAuthors().remove(index);
            }
        }
        bookRepository.save(book);
    }
}
