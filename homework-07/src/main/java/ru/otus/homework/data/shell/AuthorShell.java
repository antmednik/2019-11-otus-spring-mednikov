package ru.otus.homework.data.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.data.object.Author;
import ru.otus.homework.data.service.AuthorStorageService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
public class AuthorShell {

    private final AuthorStorageService authorStorageService;

    @ShellMethod(key = "authors", value = "get all authors")
    public void authors(){
        List<Author> authors = authorStorageService.authors();
        for (Author author : authors) {
            System.out.println(author);
        }
    }

    @ShellMethod(key = "author-save", value = "save new author")
    public void saveAuthor(@ShellOption("--name") String name) {
        authorStorageService.save(name);
    }
}
