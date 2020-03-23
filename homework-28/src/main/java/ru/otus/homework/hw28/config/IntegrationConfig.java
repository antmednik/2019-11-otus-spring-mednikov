package ru.otus.homework.hw28.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.infrastructure.Channels;
import ru.otus.homework.hw28.store.MaterialStore;

import java.util.List;

@Configuration
@IntegrationComponentScan
@EnableScheduling
public class IntegrationConfig {

    @Bean
    public IntegrationFlow forestFlow(MaterialStore woodStore) {
        return IntegrationFlows
                .from(Channels.FOREST)
                .<List<MaterialPiece>>handle((p, h) -> {
                    woodStore.acceptShipment(p);
                    return null;
                }).get();
    }

    @Bean
    public IntegrationFlow mineFlow(MaterialStore oreStore, MaterialStore coalStore){
        return IntegrationFlows
                .from(Channels.MINE)
                .split()
                .<MaterialPiece, MaterialType>route(
                        MaterialPiece::getType,
                        m -> m
                            .subFlowMapping(MaterialType.COAL, sf -> sf
                                    .<MaterialPiece>handle((p, h) -> {
                                        coalStore.acceptShipment(p);
                                        return null;
                                    }))
                            .subFlowMapping(MaterialType.ORE, sf -> sf
                            .<MaterialPiece>handle((p, h) -> {
                                oreStore.acceptShipment(p);
                                return null;
                            })))
                .get();
    }

    @Bean
    public MaterialStore woodStore() {
        return new MaterialStore(MaterialType.WOOD);
    }

    @Bean
    public MaterialStore oreStore() {
        return new MaterialStore(MaterialType.ORE);
    }

    @Bean
    public MaterialStore coalStore() {
        return new MaterialStore(MaterialType.COAL);
    }

    @Bean
    public MaterialStore metalStore() {
        return new MaterialStore(MaterialType.METAL);
    }
}
