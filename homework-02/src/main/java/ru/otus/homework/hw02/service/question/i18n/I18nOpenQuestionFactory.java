package ru.otus.homework.hw02.service.question.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw02.domain.InMemoryOpenQuestion;
import ru.otus.homework.hw02.domain.Question;
import ru.otus.homework.hw02.service.question.QuestionFactory;
import ru.otus.homework.hw02.service.question.split.SplittedQuestion;

import java.util.Locale;

@Service
public class I18nOpenQuestionFactory implements QuestionFactory {

    private final MessageSource questionsMessageSource;
    private final Locale locale;

    public I18nOpenQuestionFactory(MessageSource questionsMessageSource,
                                   @Value("${i18n.language.tag}") String languageTag) {
        this.questionsMessageSource = questionsMessageSource;
        this.locale = Locale.forLanguageTag(languageTag);
    }

    @Override
    public Question question(String rawQuestion) {

        SplittedQuestion splittedQuestion = new SplittedQuestion(rawQuestion);
        String[] parts = splittedQuestion.parts();

        return new InMemoryOpenQuestion(i18nQuestionPart(parts[0]), i18nQuestionPart(parts[1]));
    }

    private String i18nQuestionPart(String questionPart) {
        return questionsMessageSource.getMessage(questionPart, null, locale);
    }
}
