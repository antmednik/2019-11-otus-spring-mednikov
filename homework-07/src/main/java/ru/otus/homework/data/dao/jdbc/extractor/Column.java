package ru.otus.homework.data.dao.jdbc.extractor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Column {
    BOOK_ID("book_id"),

    BOOK_TITLE("book_title"),

    GENRE_ID("genre_id"),

    GENRE_NAME("genre_name"),

    AUTHOR_ID("author_id"),

    AUTHOR_NAME("author_name");

    private final String alias;

    @Override
    public String toString() {
        return alias;
    }
}
