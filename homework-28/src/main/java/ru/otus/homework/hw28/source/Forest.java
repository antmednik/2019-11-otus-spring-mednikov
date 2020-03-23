package ru.otus.homework.hw28.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.ForestLogistic;

@Service
public class Forest {

    private static final Logger LOG = LoggerFactory.getLogger(Forest.class);

    private final ForestLogistic forestLogistic;
    private final int maxWoodPerformance;
    private final MaterialSource source;

    public Forest(ForestLogistic forestLogistic,
                  @Value("${forest.wood.performance.max:10}") int maxWoodPerformance,
                  MaterialSource source) {
        this.forestLogistic = forestLogistic;
        this.maxWoodPerformance = maxWoodPerformance;
        this.source = source;
    }

    @Scheduled(fixedDelayString = "${forest.shipment.delay:5000}")
    public void cutForest() {

        var pieces = source.provide(MaterialType.WOOD, maxWoodPerformance);

        LOG.info("Wood shipment of {} piece(s)", pieces.size());

        forestLogistic.ship(pieces);
    }
}
