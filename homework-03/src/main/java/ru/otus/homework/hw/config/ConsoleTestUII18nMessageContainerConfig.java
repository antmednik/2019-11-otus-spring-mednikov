package ru.otus.homework.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.homework.hw.service.test.ui.i18n.ConsoleTestUII18nMessageContainer;

import java.util.Locale;

@Configuration
public class ConsoleTestUII18nMessageContainerConfig {

    @Bean
    public ConsoleTestUII18nMessageContainer consoleTestUII18nMessageContainer(
            MessageSource messageSource,
            @Value("${i18n.language.tag}") String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return new ConsoleTestUII18nMessageContainer(messageSource, locale);
    }
}
