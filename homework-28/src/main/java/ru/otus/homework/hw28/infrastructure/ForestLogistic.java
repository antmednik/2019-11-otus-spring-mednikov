package ru.otus.homework.hw28.infrastructure;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.hw28.domain.material.MaterialPiece;

import java.util.List;

@MessagingGateway
public interface ForestLogistic {

    @Gateway(requestChannel = Channels.FOREST)
    void ship(List<MaterialPiece> materials);
}
