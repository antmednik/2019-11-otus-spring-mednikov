package ru.otus.homework.hw28.infrastructure.logisitc;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.infrastructure.Channels;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;

import java.util.List;

@MessagingGateway
public interface WoodStoreLogistic extends BatchShippingLogistic {

    @Gateway(requestChannel = Channels.WOOD_STORE_INPUT)
    void ship(List<MaterialPiece> materials);
}
