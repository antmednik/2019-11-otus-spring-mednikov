package ru.otus.homework.hw.service.question;

import ru.otus.homework.hw.domain.Question;

public interface QuestionFactory {

    Question question(String rawQuestion);
}
