package ru.otus.homework.hw01.service.test.ui;

import lombok.AllArgsConstructor;
import ru.otus.homework.hw01.service.test.runner.TestRunner;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleTestUI {

    private final TestRunner testRunner;
    private final InputStream userInputStream;

    public ConsoleTestUI(TestRunner testRunner, InputStream userInputStream) {
        this.testRunner = testRunner;
        this.userInputStream = userInputStream;
    }

    public ConsoleTestUI(TestRunner testRunner) {
        this(testRunner, System.in);
    }

    public void runTest() {
        testRunner.start();

        try (Scanner userInputScanner = new Scanner(userInputStream)) {
            System.out.println("Вас приветствует 'SuperDuperTester'!");

            System.out.print("Введите свою фамилию: ");
            String surename = userInputScanner.nextLine();

            System.out.print("Введите своё имя: ");
            String name = userInputScanner.nextLine();

            System.out.println(String.format("Тестирование начинается, удачи, %s %s!", name, surename));

            int questionNumber = 1;
            while (testRunner.hasNextQuestion()) {
                String question = testRunner.nextQuestionText();
                System.out.print(String.format("Вопрос %d: ", questionNumber));
                System.out.println(question);
                System.out.print("Ваш ответ: ");
                String answer = userInputScanner.nextLine();

                testRunner.submitAnswer(answer);

                questionNumber++;
            }

            System.out.println(String.format("Тестирование закончено, ваш результат: %.2f %%", testRunner.resultPercent()));
        }
    }
}
