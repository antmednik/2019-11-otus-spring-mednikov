package ru.otus.homework.hw02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.homework.hw02.service.test.ui.i18n.ConsoleTestUII18nMessageContainer;

import java.util.Locale;

@Configuration
public class ConsoleTestUII18nMessageContainerConfig {

    @Bean
    public ConsoleTestUII18nMessageContainer consoleTestUII18nMessageContainer(
            MessageSource uiMessageSource,
            @Value("${i18n.language.tag}") String languageTag) {
        return new ConsoleTestUII18nMessageContainer(uiMessageSource,
                Locale.forLanguageTag(languageTag));
    }

    @Bean
    public MessageSource uiMessageSource(
            @Value("${console-test-ui.i18n.encoding.default}") String defaultEncoding,
            @Value("${console-test-ui.i18n.bundle.base-name}") String bundleBaseName) {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename(bundleBaseName);
        ms.setDefaultEncoding(defaultEncoding);
        return ms;
    }
}
