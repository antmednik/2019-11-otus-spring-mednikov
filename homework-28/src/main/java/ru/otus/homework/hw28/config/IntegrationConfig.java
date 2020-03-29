package ru.otus.homework.hw28.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;
import ru.otus.homework.hw28.factory.ArmoryWorkshop;
import ru.otus.homework.hw28.factory.Smelter;
import ru.otus.homework.hw28.infrastructure.Channels;
import ru.otus.homework.hw28.infrastructure.logisitc.*;
import ru.otus.homework.hw28.infrastructure.logisitc.base.BatchShippingLogistic;
import ru.otus.homework.hw28.store.BaseMaterialStore;
import ru.otus.homework.hw28.store.WoodStore;

import java.util.List;
import java.util.Random;

@Configuration
@IntegrationComponentScan
@EnableScheduling
public class IntegrationConfig {

    private Random random = new Random();


    /*--------------------------------------------------------------------------------------------------------------*/
    // From forest to wood store
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public BatchShippingLogistic forestOuterShippingLogistic(WoodStoreLogistic woodStoreLogistic) {
        return woodStoreLogistic;
    }

    @Bean
    public IntegrationFlow woodStoreFlow(WoodStore woodStore) {
        return IntegrationFlows
                .from(Channels.WOOD_STORE_INPUT)
                .<List<MaterialPiece>>handle((p, h) -> {
                    woodStore.acceptShipment(p);
                    return null;
                }).get();
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // From wood store to armory workshop
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public BatchShippingLogistic woodStoreOuterShippingLogistic(ArmoryWorkshopLogisticBatch armoryWorkshopLogistic) {
        return armoryWorkshopLogistic;
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // Armory workshop flow
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public IntegrationFlow armoryWorkshopFlow(ArmoryWorkshop armoryWorkshop) {
        return IntegrationFlows
                .from(Channels.ARMORY_WORKSHOP_INPUT)
                .split()
                .<MaterialPiece>handle((p, h) -> {
                    armoryWorkshop.acceptShipping(p);
                    return null;
                }).get();
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // From mine to coal and ore stores
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public IntegrationFlow mineFlow(BaseMaterialStore oreStore, BaseMaterialStore coalStore){
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

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // From coal store to smelter or armory workshop
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public BatchShippingLogistic coalStoreOuterShippingLogistic(CoalStoreLogistic coalStoreLogistic) {
        return coalStoreLogistic;
    }

    @Bean
    public IntegrationFlow coalStoreFlow(){
        return IntegrationFlows
                .from(Channels.COAL_STORE_OUTPUT)
                .split()
                .<MaterialPiece, Boolean>route(
                        (piece) -> random.nextBoolean(),
                        m -> m
                                .subFlowMapping(true, sf -> sf.channel(Channels.ARMORY_WORKSHOP_INPUT))
                                .subFlowMapping(false, sf -> sf.channel(Channels.SMELTER_INPUT)))
                .get();
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // From ore store to smelter
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public BatchShippingLogistic oreStoreOuterShippingLogistic(SmelterLogistic smelterLogistic) {
        return smelterLogistic;
    }

    @Bean
    public IntegrationFlow smelterFLow(Smelter smelter) {
        return IntegrationFlows
                .from(Channels.SMELTER_INPUT)
                .split()
                .<MaterialPiece>handle((p, h) -> {
                    smelter.acceptShipping(p);
                    return null;
                }).get();
    }

    /*--------------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------------*/
    // From smelter to armory workshop
    /*--------------------------------------------------------------------------------------------------------------*/

    @Bean
    public BatchShippingLogistic smelterOuterShippingLogistic(ArmoryWorkshopLogisticBatch armoryWorkshopLogistic) {
        return armoryWorkshopLogistic;
    }

    /*--------------------------------------------------------------------------------------------------------------*/
}
