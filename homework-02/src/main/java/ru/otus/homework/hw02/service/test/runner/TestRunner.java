package ru.otus.homework.hw02.service.test.runner;

import lombok.NonNull;

public interface TestRunner {

    void start();

    boolean hasNextQuestion();

    String nextQuestionText();

    void submitAnswer(@NonNull String answer);

    double resultPercent();
}
