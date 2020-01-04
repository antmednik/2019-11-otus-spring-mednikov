package ru.otus.homework.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.hw.service.test.runner.TestRunner;
import ru.otus.homework.hw.service.test.ui.ConsoleTestUI;
import ru.otus.homework.hw.service.test.ui.i18n.ConsoleTestUII18nMessageContainer;

@Configuration
public class ConsoleTestUIConfig {

    @Bean
    ConsoleTestUI consoleTestUI(TestRunner testRunner,
                                ConsoleTestUII18nMessageContainer consoleTestUII18nMessageContainer) {
        return new ConsoleTestUI(testRunner, consoleTestUII18nMessageContainer);
    }
}
