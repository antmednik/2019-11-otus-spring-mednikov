package ru.otus.homework.hw.service.test.runner;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.hw.domain.Question;
import ru.otus.homework.hw.service.question.QuestionCollectionFactory;
import ru.otus.homework.hw.service.test.TestConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LinearTestRunnerTest {

    @MockBean
    private QuestionCollectionFactory questionCollectionFactory;

    @Autowired
    private LinearTestRunner sut;

    @Test
    public void runTestWith1CorrectAnswerFrom2(){

        List<Question> questions = new ArrayList<>();

        Question question1 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(false);
        questions.add(question1);
        Question question2 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(true);
        questions.add(question2);

        when(questionCollectionFactory.questions()).thenReturn(questions);

        sut.start();

        while(sut.hasNextQuestion()){
            sut.submitAnswer(UUID.randomUUID().toString());
        }

        assertThat(sut.resultPercent()).isEqualTo(50.0f, Offset.offset(0.001));
    }

    @Test
    public void runTestWith2CorrectAnswerFrom3(){

        List<Question> questions = new ArrayList<>();

        Question question1 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(false);
        questions.add(question1);
        Question question2 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(true);
        questions.add(question2);
        Question question3 = mock(Question.class);
        when(question1.answerIsCorrect(any())).thenReturn(true);
        questions.add(question3);

        when(questionCollectionFactory.questions()).thenReturn(questions);

        sut.start();

        while(sut.hasNextQuestion()){
            sut.submitAnswer(UUID.randomUUID().toString());
        }

        assertThat(sut.resultPercent()).isEqualTo(33.333f, Offset.offset(0.001));
    }

    @Test
    public void runTestWithoutQuestionsResultPercentIs100(){
        when(questionCollectionFactory.questions()).thenReturn(new ArrayList<>());

        sut.start();

        while(sut.hasNextQuestion()){
            sut.submitAnswer(UUID.randomUUID().toString());
        }

        assertThat(sut.resultPercent()).isEqualTo(100.0f, Offset.offset(0.001));
    }
}
