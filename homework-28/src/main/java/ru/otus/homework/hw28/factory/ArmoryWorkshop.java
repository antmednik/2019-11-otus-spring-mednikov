package ru.otus.homework.hw28.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.armory.ArmoryType;
import ru.otus.homework.hw28.domain.material.MaterialPiece;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ArmoryWorkshop {

    private static final Logger LOG = LoggerFactory.getLogger(ArmoryWorkshop.class);

    // WOOD - METAL- COAL
    private static final Map<ArmoryType, List<Integer>> COSTS = Map.of(
            ArmoryType.SHIELD, List.of(10, 5, 2),
            ArmoryType.SPEAR, List.of(8, 3, 4),
            ArmoryType.SWORD, List.of(3, 20, 8));

    private int metalPiecesCount = 0;
    private int woodPiecesCount = 0;
    private int coalPiecesCount = 0;
    private final Random random = new Random();

    public void acceptShipping(MaterialPiece materialPiece) {
        switch (materialPiece.getType()) {
            case METAL: metalPiecesCount++; break;
            case WOOD: woodPiecesCount++; break;
            case COAL: coalPiecesCount++; break;
            default: LOG.warn("Shipment '{}' rejected", materialPiece);
        }
    }

    @Scheduled(fixedDelayString = "${armory-workshop.craft.delay:5000}")
    public void craft() {
        var armoryType = ArmoryType.values()[random.nextInt(ArmoryType.values().length)];
        var costs = COSTS.get(armoryType);

        int requiredWoodCount = costs.get(0);
        int requiredMetalCount = costs.get(1);
        int requiredCoalCount = costs.get(2);

        if (requiredWoodCount <= woodPiecesCount
        && requiredMetalCount <= metalPiecesCount
        && requiredCoalCount <= coalPiecesCount) {

            woodPiecesCount -= requiredWoodCount;
            metalPiecesCount -= requiredMetalCount;
            coalPiecesCount -= requiredCoalCount;

            LOG.info("Crafted '{}' using - wood: {}, metal: {}, coal: {}; current stash - wood: {}, metal: {}, coal: {}",
                    armoryType,
                    requiredWoodCount, requiredMetalCount, requiredCoalCount,
                    woodPiecesCount, metalPiecesCount, coalPiecesCount);
        } else {
            LOG.warn("Not enough resources for '{}' required - wood: {}, metal: {}, coal: {}; current stash - wood: {}, metal: {}, coal: {}",
                    armoryType,
                    requiredWoodCount, requiredMetalCount, requiredCoalCount,
                    woodPiecesCount, metalPiecesCount, coalPiecesCount);
        }
    }
}
