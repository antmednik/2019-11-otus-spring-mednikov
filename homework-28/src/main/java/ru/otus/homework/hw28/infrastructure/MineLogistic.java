package ru.otus.homework.hw28.infrastructure;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.hw28.domain.material.MaterialPiece;

import java.util.List;

@MessagingGateway
public interface MineLogistic {

    @Gateway(requestChannel = Channels.MINE)
    void ship(List<MaterialPiece> materials);
}
