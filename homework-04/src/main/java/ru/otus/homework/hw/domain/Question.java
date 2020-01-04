package ru.otus.homework.hw.domain;

public interface Question {
    String text();

    boolean answerIsCorrect(String answerCandidate);
}
