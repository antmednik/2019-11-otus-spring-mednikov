package ru.otus.homework.hw01.domain;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class InMemoryOpenQuestion implements Question {

    private final String question;
    private final String answer;

    @Override
    public String text() {
        return question;
    }

    @Override
    public boolean answerIsCorrect(@NonNull String answerCandidate) {
        return answer.equals(answerCandidate);
    }
}
