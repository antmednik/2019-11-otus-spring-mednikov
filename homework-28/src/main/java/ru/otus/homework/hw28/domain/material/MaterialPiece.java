package ru.otus.homework.hw28.domain.material;

public class MaterialPiece {

    private final MaterialType type;

    public MaterialPiece(MaterialType type) {
        this.type = type;
    }

    public MaterialType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MaterialPiece{" +
                "type=" + type +
                '}';
    }
}
