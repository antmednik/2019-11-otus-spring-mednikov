package ru.otus.homework.hw28.source;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.homework.hw28.domain.material.MaterialPiece;
import ru.otus.homework.hw28.domain.material.MaterialType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MaterialSource {

    public List<MaterialPiece> provide(MaterialType type, int maxPerformance) {
        final int piecesCount = RandomUtils.nextInt(1, maxPerformance);
        return Stream.generate(() -> new MaterialPiece(type))
                .limit(piecesCount)
                .collect(Collectors.toList());
    }
}
