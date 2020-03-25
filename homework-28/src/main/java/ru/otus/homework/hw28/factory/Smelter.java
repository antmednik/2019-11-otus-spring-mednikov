package ru.otus.homework.hw28.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;

import java.util.List;

/**
 * Smelter is used for transformation of {@link ru.otus.homework.hw28.domain.material.MaterialType}:
 * metal is crafted from ore and coal
 */
@Service
public class Smelter {

    private static final Logger LOG = LoggerFactory.getLogger(Smelter.class);

    private static final int COAL_REQUIRED_FOR_METAL = 2;
    private static final int ORE_REQUIRED_FOR_METAL = 3;

    private final BatchShippingLogistic shippingLogistic;

    private int coalPiecesCount = 0;
    private int orePiecesCount = 0;

    public Smelter(BatchShippingLogistic smelterOuterShippingLogistic) {
        this.shippingLogistic = smelterOuterShippingLogistic;
    }

    public void acceptShipping(MaterialPiece materialPiece) {
        switch (materialPiece.getType()) {
            case COAL: {
                coalPiecesCount++;
                LOG.info("Coal piece accepted.");
                break;
            }
            case ORE: {
                orePiecesCount++;
                LOG.info("Ore piece accepted.");
                break;
            }
            default: LOG.warn("Shipment '{}' rejected", materialPiece);
        }
    }

    @Scheduled(fixedDelayString = "${smelter.craft.metal.delay:1000}")
    public void craftMetal() {

        if (COAL_REQUIRED_FOR_METAL <= coalPiecesCount
            && ORE_REQUIRED_FOR_METAL <= orePiecesCount) {

            coalPiecesCount -= COAL_REQUIRED_FOR_METAL;
            orePiecesCount -= ORE_REQUIRED_FOR_METAL;

            LOG.info("Metal piece crafted");

            shippingLogistic.ship(List.of(new MaterialPiece(MaterialType.METAL)));
        }
    }
}
