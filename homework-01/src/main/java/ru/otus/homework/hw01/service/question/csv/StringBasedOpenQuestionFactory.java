package ru.otus.homework.hw01.service.question.csv;

import lombok.NonNull;
import ru.otus.homework.hw01.domain.InMemoryOpenQuestion;
import ru.otus.homework.hw01.domain.Question;
import ru.otus.homework.hw01.service.question.QuestionFactory;

public class StringBasedOpenQuestionFactory implements QuestionFactory {

    private static final String FIELD_SEPARATOR = ";";

    @Override
    public Question question(@NonNull String rawQuestion) {
        String[] rawQuestionParts = rawQuestion.split(FIELD_SEPARATOR);
        String questionText = rawQuestionParts[0];
        String answerText = rawQuestionParts[1];

        return new InMemoryOpenQuestion(questionText, answerText);
    }
}
