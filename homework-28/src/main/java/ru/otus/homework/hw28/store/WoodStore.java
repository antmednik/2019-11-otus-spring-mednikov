package ru.otus.homework.hw28.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;
import ru.otus.homework.hw28.source.MaterialSource;

@Service
public class WoodStore extends BaseMaterialStore {

    public WoodStore(@Value("${wood-store.batch.size:15}") int batchSize,
                     BatchShippingLogistic woodStoreOuterShippingLogistic,
                     MaterialSource materialSource) {
        super(batchSize, woodStoreOuterShippingLogistic, materialSource);
    }

    @Override
    protected MaterialType materialType() {
        return MaterialType.WOOD;
    }

    @Scheduled(fixedDelayString = "${wood-store.shipment.delay:5000}")
    public void ship() {
        shipToOuter();
    }
}
