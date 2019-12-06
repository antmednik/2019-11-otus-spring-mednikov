package ru.otus.homework.hw02;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.otus.homework.hw02.service.test.ui.ConsoleTestUI;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class Hw02Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(Hw02Application.class);
		context.refresh();

		ConsoleTestUI consoleUI = context.getBean(ConsoleTestUI.class);
		consoleUI.runTest();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
