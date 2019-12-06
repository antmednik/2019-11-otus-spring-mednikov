package ru.otus.homework.hw02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.hw02.service.question.QuestionCollectionFactory;
import ru.otus.homework.hw02.service.question.QuestionFactory;
import ru.otus.homework.hw02.service.question.csv.CsvBasedQuestionCollectionFactory;

@Configuration
public class QuestionCollectionFactoryConfig {

    @Bean
    public QuestionCollectionFactory questionCollectionFactory(QuestionFactory questionFactory,
                                                               @Value("${questions.file.path}") String questionsFilePath) {
        return new CsvBasedQuestionCollectionFactory(questionFactory, questionsFilePath);
    }
}
