package pl.wozniaktomek.util;

public enum DataGeneratorSector {
    /* Predefined sectors for data editor generator */
    N(-4d, 4d, 2d, 8d),
    S(-4d, 4d, -8d, -2d),
    W(-8d, -2d, -4d, 4d),
    E(2d, 8d, -4d, 4d),
    NW(-8d, -2d, 2d, 8d),
    NE(2d, 8d, 2d, 8d),
    SW(-8d, -2d, -8d, -2d),
    SE(2d, 8d, -8d, -2d);

    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;

    DataGeneratorSector(Double minX, Double maxX, Double minY, Double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /* Getters */
    public Double getMinX() {
        return minX;
    }

    public Double getMaxX() {
        return maxX;
    }

    public Double getMinY() {
        return minY;
    }

    public Double getMaxY() {
        return maxY;
    }
}
