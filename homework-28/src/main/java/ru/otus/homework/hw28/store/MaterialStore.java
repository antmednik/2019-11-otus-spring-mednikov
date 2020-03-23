package ru.otus.homework.hw28.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;

import java.util.List;

public class MaterialStore {

    private static final Logger log = LoggerFactory.getLogger(MaterialStore.class);

    private final MaterialType materialType;
    private long storedCount;

    public MaterialStore(MaterialType materialType) {
        this.materialType = materialType;
        this.storedCount = 0;
    }

    public void acceptShipment(MaterialPiece material) {
        acceptShipment(List.of(material));
    }

    public void acceptShipment(List<MaterialPiece> materials) {
        long acceptedCount = materials.stream()
                .filter(m -> m.getType() == materialType)
                .count();
        if (acceptedCount < materials.size()) {
            log.warn("Rejected {} piece(s); must be of material '{}'.",
                    materials.size() - acceptedCount,
                    materialType);
        }

        storedCount += acceptedCount;

        log.info("[{} store] Shiped {} piece(s), total: {}",
                materialType, acceptedCount, storedCount);
    }
}
