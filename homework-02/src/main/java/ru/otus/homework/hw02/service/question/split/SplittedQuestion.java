package ru.otus.homework.hw02.service.question.split;

public class SplittedQuestion {

    private static final String FIELD_SEPARATOR = ";";

    private final String rawQuestion;

    public SplittedQuestion(String rawQuestion) {
        this.rawQuestion = rawQuestion;
    }

    public String[] parts() {
        return rawQuestion.split(FIELD_SEPARATOR);
    }
}
