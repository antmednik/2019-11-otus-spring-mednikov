package ru.otus.homework.hw28.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;
import ru.otus.homework.hw28.source.MaterialSource;

import java.util.List;

public abstract class BaseMaterialStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int batchSize;
    private final BatchShippingLogistic outerShippingLogistic;
    private final MaterialSource materialSource;

    private long storedCount;

    protected abstract MaterialType materialType();

    public BaseMaterialStore(int batchSize, BatchShippingLogistic outerShippingLogistic, MaterialSource materialSource) {
        this.batchSize = batchSize;
        this.outerShippingLogistic = outerShippingLogistic;
        this.materialSource = materialSource;

        this.storedCount = 0;
    }

    public void acceptShipment(MaterialPiece material) {
        acceptShipment(List.of(material));
    }

    public void acceptShipment(List<MaterialPiece> materials) {
        long acceptedCount = materials.stream()
                .filter(m -> m.getType() == materialType())
                .count();
        if (acceptedCount < materials.size()) {
            log.warn("Rejected {} piece(s); must be of material '{}'.",
                    materials.size() - acceptedCount,
                    materialType());
        }

        storedCount += acceptedCount;

        log.info("Received {} piece(s), total: {}", acceptedCount, storedCount);
    }

    void shipToOuter() {
        if (storedCount > batchSize) {
            storedCount -= batchSize;

            var pieces = materialSource.provide(materialType(), batchSize);

            log.info("Sent {} piece(s), total: {}", batchSize, storedCount);

            outerShippingLogistic.ship(pieces);
        }
    }
}
