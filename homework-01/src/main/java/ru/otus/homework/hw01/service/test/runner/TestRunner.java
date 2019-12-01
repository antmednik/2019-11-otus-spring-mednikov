package ru.otus.homework.hw01.service.test.runner;

import lombok.NonNull;
import ru.otus.homework.hw01.domain.Question;

public interface TestRunner {

    void start();

    boolean hasNextQuestion();

    String nextQuestionText();

    void submitAnswer(@NonNull String answer);

    double resultPercent();
}
