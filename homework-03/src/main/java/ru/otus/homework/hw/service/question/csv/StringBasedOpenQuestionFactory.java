package ru.otus.homework.hw.service.question.csv;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw.domain.InMemoryOpenQuestion;
import ru.otus.homework.hw.domain.Question;
import ru.otus.homework.hw.service.question.QuestionFactory;
import ru.otus.homework.hw.service.question.split.SplittedQuestion;

public class StringBasedOpenQuestionFactory implements QuestionFactory {

    @Override
    public Question question(@NonNull String rawQuestion) {
        String[] rawQuestionParts = new SplittedQuestion(rawQuestion).parts();
        String questionText = rawQuestionParts[0];
        String answerText = rawQuestionParts[1];

        return new InMemoryOpenQuestion(questionText, answerText);
    }
}
