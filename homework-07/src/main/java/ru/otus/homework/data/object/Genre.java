package ru.otus.homework.data.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Genre {

    private final UUID id;

    private final String name;
}
