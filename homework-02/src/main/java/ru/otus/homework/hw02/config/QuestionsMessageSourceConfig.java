package ru.otus.homework.hw02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class QuestionsMessageSourceConfig {

    @Bean
    public MessageSource questionsMessageSource(
            @Value("${questions.i18n.encoding.default}") String defaultEncoding,
            @Value("${questions.i18n.bundle.base-name}") String bundleBaseName) {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename(bundleBaseName);
        ms.setDefaultEncoding(defaultEncoding);
        return ms;
    }
}
