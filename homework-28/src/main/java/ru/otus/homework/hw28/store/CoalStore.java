package ru.otus.homework.hw28.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;
import ru.otus.homework.hw28.source.MaterialSource;

@Service
public class CoalStore extends BaseMaterialStore {

    public CoalStore(@Value("${coal-store.batch.size:15}") int batchSize,
                     BatchShippingLogistic coalStoreOuterShippingLogistic,
                     MaterialSource materialSource) {
        super(batchSize, coalStoreOuterShippingLogistic, materialSource);
    }

    @Override
    protected MaterialType materialType() {
        return MaterialType.COAL;
    }

    @Scheduled(fixedDelayString = "${coal-store.shipment.delay:5000}")
    public void ship() {
        shipToOuter();
    }
}
