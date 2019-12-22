package ru.otus.homework.hw.service.test.ui.i18n;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ConsoleTestUII18nMessageContainer {

    private static final String RESOURCE_PROP_BASE_PREFIX = "console-test-ui.message.";

    private final MessageSource userMessageSource;
    private final Locale locale;

    public ConsoleTestUII18nMessageContainer(MessageSource userMessageSource, Locale locale) {
        this.userMessageSource = userMessageSource;
        this.locale = locale;
    }

    public String greeting() {
        return i18nMessage("greeting");
    }

    public String surenameRequest() {
        return i18nMessage("surename-request");
    }

    public String nameRequest() {
        return i18nMessage("name-request");
    }

    public String testStarting(String name, String surename) {
        return String.format(i18nMessage("test-starting"), name, surename);
    }

    public String questionIntro(int questionNumber) {
        return String.format(i18nMessage("question-prefix"), questionNumber);
    }

    public String answerIntro() {
        return i18nMessage("answer-prefix");
    }

    public String testFinished(double resultPercent) {
        return String.format(i18nMessage("test-finished"), resultPercent);
    }

    private String i18nMessage(String resourcePropertyName) {
        return userMessageSource.getMessage(
                fullResourcePropertyName(resourcePropertyName), null, locale);
    }

    private String fullResourcePropertyName(String resourcePropertyName) {
        return RESOURCE_PROP_BASE_PREFIX + resourcePropertyName;
    }
}
