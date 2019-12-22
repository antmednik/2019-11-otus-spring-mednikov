package ru.otus.homework.hw.service.test.ui;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.hw.service.test.runner.TestRunner;
import ru.otus.homework.hw.service.test.ui.i18n.ConsoleTestUII18nMessageContainer;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleTestUI {

    private final TestRunner testRunner;
    private final InputStream userInputStream;
    private final PrintStream userOutputStream;
    private final ConsoleTestUII18nMessageContainer consoleTestUII18NMessageContainer;

    public ConsoleTestUI(TestRunner testRunner, ConsoleTestUII18nMessageContainer consoleTestUII18NMessageContainer) {
        this(testRunner, System.in, System.out, consoleTestUII18NMessageContainer);
    }

    public void runTest() {
        testRunner.start();

        Scanner userInputScanner = new Scanner(userInputStream);
        userOutputStream.println(consoleTestUII18NMessageContainer.greeting());

        userOutputStream.print(consoleTestUII18NMessageContainer.surenameRequest());
        String surename = userInputScanner.nextLine();

        userOutputStream.print(consoleTestUII18NMessageContainer.nameRequest());
        String name = userInputScanner.nextLine();

        userOutputStream.println(consoleTestUII18NMessageContainer.testStarting(name, surename));

        int questionNumber = 1;
        while (testRunner.hasNextQuestion()) {
            String question = testRunner.nextQuestionText();
            userOutputStream.print(consoleTestUII18NMessageContainer.questionIntro(questionNumber));
            userOutputStream.println(question);
            userOutputStream.print(consoleTestUII18NMessageContainer.answerIntro());
            String answer = userInputScanner.nextLine();

            testRunner.submitAnswer(answer);

            questionNumber++;
        }

        userOutputStream.println(consoleTestUII18NMessageContainer.testFinished(testRunner.resultPercent()));
    }
}
