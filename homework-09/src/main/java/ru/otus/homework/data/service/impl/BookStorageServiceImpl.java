package ru.otus.homework.data.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.data.dao.*;
import ru.otus.homework.data.entity.Book;
import ru.otus.homework.data.service.BookStorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookStorageServiceImpl implements BookStorageService {

    private final BookDao bookDao;
    private final BookAuthorDao bookAuthorDao;
    private final BookGenreDao bookGenreDao;

    public Book save(String title, List<UUID> authorsIds, List<UUID> genresIds) {

        Book storedBook = bookDao.save(new Book(UUID.randomUUID(), title));

        connectBookWithAuthors(storedBook.getId(), authorsIds);

        connectBookWithGenres(storedBook.getId(), genresIds);

        return bookById(storedBook.getId()).get();
    }

    private void connectBookWithGenres(UUID bookId, List<UUID> genresIds) {
        for (UUID genreId : genresIds) {
            bookGenreDao.saveBookGenreConnection(bookId, genreId);
        }
    }

    private void connectBookWithAuthors(UUID bookId, List<UUID> authorsIds) {
        for (UUID authorId : authorsIds) {
            bookAuthorDao.saveBookAuthorConnection(bookId, authorId);
        }
    }

    public List<Book> books() {
        return bookDao.books();
    }

    public Optional<Book> bookById(UUID bookId) {
        return bookDao.bookById(bookId);
    }

    public void deleteBookById(UUID bookId){
        bookAuthorDao.deleteBookAuthorConnection(bookId);

        bookGenreDao.deleteBookGenreConnection(bookId);

        bookDao.deleteById(bookId);
    }

    @Override
    public boolean update(UUID bookId, String newTitle, List<UUID> authorsIds, List<UUID> genresIds) {
        boolean updated = bookDao.updateTitle(bookId, newTitle);
        if (!updated) {
            return false;
        }

        bookAuthorDao.deleteBookAuthorConnection(bookId);
        connectBookWithAuthors(bookId, authorsIds);

        bookGenreDao.deleteBookGenreConnection(bookId);
        connectBookWithGenres(bookId, genresIds);

        return true;
    }
}
