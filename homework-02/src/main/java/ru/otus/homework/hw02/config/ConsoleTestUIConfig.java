package ru.otus.homework.hw02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.hw02.service.test.runner.TestRunner;
import ru.otus.homework.hw02.service.test.ui.ConsoleTestUI;

@Configuration
public class ConsoleTestUIConfig {

    @Bean
    ConsoleTestUI consoleTestUI(TestRunner testRunner) {
        return new ConsoleTestUI(testRunner);
    }
}
