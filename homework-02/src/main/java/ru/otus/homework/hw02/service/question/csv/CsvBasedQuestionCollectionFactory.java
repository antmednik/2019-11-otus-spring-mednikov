package ru.otus.homework.hw02.service.question.csv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw02.domain.Question;
import ru.otus.homework.hw02.exception.QuestionReadException;
import ru.otus.homework.hw02.service.question.QuestionCollectionFactory;
import ru.otus.homework.hw02.service.question.QuestionFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvBasedQuestionCollectionFactory implements QuestionCollectionFactory {

    private final QuestionFactory questionFactory;
    private final String questionsFilePath;

    public CsvBasedQuestionCollectionFactory(QuestionFactory questionFactory,
                                             String questionsFilePath) {
        this.questionFactory = questionFactory;
        this.questionsFilePath = questionsFilePath;
    }

    @Override
    public List<Question> questions() {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = questionsDataInputStream();
             Scanner questionScanner = new Scanner(inputStream)) {

            while (questionScanner.hasNextLine()) {
                String rawQuestion = questionScanner.nextLine();
                questions.add(questionFactory.question(rawQuestion));
            }
        } catch (Exception ex) {
            throw new QuestionReadException(ex);
        }

        return questions;
    }

    private InputStream questionsDataInputStream() {
        return getClass().getClassLoader().getResourceAsStream(questionsFilePath);
    }
}
