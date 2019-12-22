package ru.otus.homework.hw.service.test.runner;

import lombok.NonNull;

public interface TestRunner {

    void start();

    boolean hasNextQuestion();

    String nextQuestionText();

    void submitAnswer(@NonNull String answer);

    double resultPercent();
}
