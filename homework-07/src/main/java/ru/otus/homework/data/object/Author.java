package ru.otus.homework.data.object;

import lombok.*;

import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Author {

    private final UUID id;

    private String name;
}
