package ru.otus.homework.hw01;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.hw01.service.test.ui.ConsoleTestUI;

public class Hw01Application {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		ConsoleTestUI consoleUI = context.getBean(ConsoleTestUI.class);
		consoleUI.runTest();
	}
}
