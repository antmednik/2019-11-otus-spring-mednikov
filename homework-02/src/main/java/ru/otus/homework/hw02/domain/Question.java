package ru.otus.homework.hw02.domain;

public interface Question {
    String text();

    boolean answerIsCorrect(String answerCandidate);
}
