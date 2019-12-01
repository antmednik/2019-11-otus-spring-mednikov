package ru.otus.homework.hw01.domain;

public interface Question {
    String text();

    boolean answerIsCorrect(String answerCandidate);
}
