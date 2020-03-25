package ru.otus.homework.hw28.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.logisitc.MineLogistic;

import java.util.ArrayList;

@Service
public class Mine {

    private static final Logger LOG = LoggerFactory.getLogger(Mine.class);

    private final MineLogistic mineLogistic;
    private final int maxOrePerformance;
    private final int maxCoalPerformance;
    private final MaterialSource materialSource;

    public Mine(MineLogistic mineLogistic,
                @Value("${mine.ore.performance.max:10}") int maxOrePerformance,
                @Value("${mine.coal.performance.max:10}") int maxCoalPerformance,
                MaterialSource materialSource) {
        this.mineLogistic = mineLogistic;
        this.maxOrePerformance = maxOrePerformance;
        this.maxCoalPerformance = maxCoalPerformance;
        this.materialSource = materialSource;
    }

    @Scheduled(fixedDelayString = "${mine.shipment.delay:1000}")
    public void mine() {

        var coalPieces = materialSource.provide(MaterialType.COAL, maxCoalPerformance);
        var orePieces = materialSource.provide(MaterialType.ORE, maxOrePerformance);

        var pieces = new ArrayList<MaterialPiece>();
        pieces.addAll(coalPieces);
        pieces.addAll(orePieces);

        LOG.info("Shipment of {} ore piece(s) and {} coal piece(s)",
                orePieces.size(), coalPieces.size());

        mineLogistic.ship(pieces);
    }
}
