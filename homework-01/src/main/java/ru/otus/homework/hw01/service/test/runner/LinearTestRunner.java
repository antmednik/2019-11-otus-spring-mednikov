package ru.otus.homework.hw01.service.test.runner;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import ru.otus.homework.hw01.domain.Question;
import ru.otus.homework.hw01.service.question.QuestionCollectionFactory;

import java.util.List;

public class LinearTestRunner implements TestRunner {

    private static final int DEFAULT_QUESTION_INDEX = -1;
    private static final int INITIAL_RUN_SCORE = 0;
    private static final float MAX_SCORE_PERCENT = 100.0f;

    private final QuestionCollectionFactory questionCollectionFactory;
    private List<Question> storedQuestions;
    private int activeQuestionIndex;
    private int runScore;

    public LinearTestRunner(QuestionCollectionFactory questionCollectionFactory) {
        this.questionCollectionFactory = questionCollectionFactory;
    }

    @Override
    public void start() {
        activeQuestionIndex = DEFAULT_QUESTION_INDEX;
        runScore = INITIAL_RUN_SCORE;
    }

    @Override
    public boolean hasNextQuestion() {
        List<Question> questions = questions();

        if (activeQuestionIndex == questions.size()) {
            return false;
        }

        return ++activeQuestionIndex < questions.size();
    }

    @Override
    public String nextQuestionText() {
        if (activeQuestionIndex == DEFAULT_QUESTION_INDEX) {
            throw new IllegalStateException("Use hasNextQuestion() to proceed to next text.");
        }

        return activeQuestion().text();
    }

    @Override
    public void submitAnswer(@NonNull String answer) {
        Question question = activeQuestion();
        if (question.answerIsCorrect(answer)) {
            runScore++;
        }
    }

    @Override
    public double resultPercent() {
        List<Question> questions = questions();
        if (questions.size() == 0) {
            return MAX_SCORE_PERCENT;
        }

        return ((float)runScore / questions.size()) * 100.0f;
    }

    private List<Question> questions() {
        if (storedQuestions == null) {
            storedQuestions = questionCollectionFactory.questions();
        }

        return storedQuestions;
    }

    private Question activeQuestion() {
        return questions().get(activeQuestionIndex);
    }
}
