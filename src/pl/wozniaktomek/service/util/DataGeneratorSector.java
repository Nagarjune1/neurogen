package pl.wozniaktomek.service.util;

public enum DataGeneratorSector {
    N(1, -4d, 4d, 2d, 8d),
    S(2, -4d, 4d, -8d, -2d),
    W(3, -8d, -2d, -4d, 4d),
    E(4, 2d, 8d, -4d, 4d),
    NW(5, -8d, -2d, 2d, 8d),
    NE(6, 2d, 8d, 2d, 8d),
    SW(7, -8d, -2d, -8d, -2d),
    SE(8, 2d, 8d, -8d, -2d);

    private Integer number;
    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;

    DataGeneratorSector(Integer number, Double minX, Double maxX, Double minY, Double maxY) {
        this.number = number;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

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
