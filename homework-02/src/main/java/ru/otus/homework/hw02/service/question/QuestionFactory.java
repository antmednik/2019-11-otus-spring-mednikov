package ru.otus.homework.hw02.service.question;

import ru.otus.homework.hw02.domain.Question;

public interface QuestionFactory {

    Question question(String rawQuestion);
}
