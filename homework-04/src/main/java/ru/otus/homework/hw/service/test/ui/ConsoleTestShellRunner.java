package ru.otus.homework.hw.service.test.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true) // needed for tests
public class ConsoleTestShellRunner {

    private final ConsoleTestUI consoleTestUI;

    @ShellMethod("run-test")
    public void runTest() {
        consoleTestUI.runTest();
    }
}
