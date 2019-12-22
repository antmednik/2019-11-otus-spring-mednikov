package ru.otus.homework.hw.service.test.ui;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

//@Service
@AllArgsConstructor
public class ConsoleTestUIRunner implements CommandLineRunner {

    private final ConsoleTestUI consoleTestUI;

    @Override
    public void run(String... args) throws Exception {
        consoleTestUI.runTest();
    }
}
