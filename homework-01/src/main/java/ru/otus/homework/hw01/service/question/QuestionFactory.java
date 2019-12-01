package ru.otus.homework.hw01.service.question;

import ru.otus.homework.hw01.domain.Question;

public interface QuestionFactory {

    Question question(String rawQuestion);
}
