package ru.otus.homework.data.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.data.object.Genre;
import ru.otus.homework.data.service.GenreStorageService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
public class GenreShell {

    private final GenreStorageService genreStorageService;

    @ShellMethod(key = "genres", value = "get all genres")
    public void genres(){
        List<Genre> genres = genreStorageService.genres();
        for (Genre genre : genres) {
            System.out.println(genre);
        }
    }

    @ShellMethod(key = "genre-save", value = "save new genre")
    public void saveGenre(@ShellOption("--name") String name) {
        genreStorageService.save(name);
    }
}
