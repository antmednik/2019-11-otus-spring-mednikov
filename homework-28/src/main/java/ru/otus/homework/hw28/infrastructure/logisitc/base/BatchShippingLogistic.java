package ru.otus.homework.hw28.infrastructure.logisitc.base;

import ru.otus.homework.hw28.domain.material.MaterialPiece;

import java.util.List;

public interface BatchShippingLogistic {

    void ship(List<MaterialPiece> materials);
}
