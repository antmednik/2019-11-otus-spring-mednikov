package ru.otus.homework.hw01.service.test.runner;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.homework.hw01.domain.Question;
import ru.otus.homework.hw01.service.question.QuestionCollectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

public class LinearTestRunnerTest {

    @Test
    public void runTestWith1CorrectAnswerFromOne(){

        QuestionCollectionFactory questionCollectionFactory = mock(QuestionCollectionFactory.class);
        List<Question> questions = new ArrayList<>();

        Question question1 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(false);
        questions.add(question1);
        Question question2 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(true);
        questions.add(question2);

        when(questionCollectionFactory.questions()).thenReturn(questions);

        LinearTestRunner sut = new LinearTestRunner(questionCollectionFactory);

        sut.start();

        while(sut.hasNextQuestion()){
            sut.submitAnswer(UUID.randomUUID().toString());
        }

        assertThat(sut.resultPercent()).isEqualTo(50.0f, Offset.offset(0.001));
    }
}
