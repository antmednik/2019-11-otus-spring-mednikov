package ru.otus.homework.data.entity.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Document
public class Comment implements Comparable<Comment> {

    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return text.equals(comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public int compareTo(Comment o) {

        if (o == null) {
            return -1;
        }

        return getText().compareTo(o.getText());
    }
}
