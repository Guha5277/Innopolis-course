package task01.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Separator {
    GameField root;
    private int chunks;
    Coordinates[] subFieldsCoordinates;
    int debugIndex = 0;

    public Separator(GameField root, int chunks) {
        this.root = root;
        this.chunks = chunks;
        subFieldsCoordinates = new Coordinates[]{new Coordinates(0, root.getX() - 1, 0, root.getY() - 1)};
        subFieldsCoordinates = separate(subFieldsCoordinates);
    }

    private Coordinates[] separate(Coordinates[] coordinates) {
        if (chunks == coordinates.length) {
            return coordinates;
        }
        ArrayList<Coordinates> result = new ArrayList<>(Arrays.asList(coordinates));

        Coordinates maxAreaCord = result.stream()
                .max(Comparator.comparingInt(Coordinates::getArea))
                .orElseThrow(() -> new RuntimeException("Separator error!"));

        Coordinates[] separatedChunks;

        if (maxAreaCord.getXLength() > maxAreaCord.getYLength()) {
            separatedChunks = separateByX(maxAreaCord);
        } else {
            separatedChunks = separateByY(maxAreaCord);
        }

        result.remove(maxAreaCord);
        result.addAll(Arrays.asList(separatedChunks));

        return separate(result.toArray(new Coordinates[0]));
    }

    public Coordinates[] getSubFieldsCoordinates() {
        return subFieldsCoordinates;
    }

    private Coordinates[] separateByX(Coordinates cord) {
        Coordinates[] result = new Coordinates[2];

        int xTo = cord.getXTo() - (cord.getXLength() / 2);
        result[0] = new Coordinates(cord.getXFrom(), xTo, cord.getYFrom(), cord.getYTo());
        result[1] = new Coordinates(xTo + 1, cord.getXTo(), cord.getYFrom(), cord.getYTo());
        return result;
    }

    private Coordinates[] separateByY(Coordinates cord) {
        Coordinates[] result = new Coordinates[2];

        int yTo = cord.getYTo() - (cord.getYLength() / 2);
        result[0] = new Coordinates(cord.getXFrom(), cord.getXTo(), cord.getYFrom(), yTo);
        result[1] = new Coordinates(cord.getXFrom(), cord.getXTo(), yTo + 1, cord.getYTo());
        return result;
    }

    public class Coordinates {
        int xFrom;
        int xTo;
        int yFrom;
        int yTo;
        int xLength;
        int yLength;

        public Coordinates(int xFrom, int xTo, int yFrom, int yTo) {
            if (xFrom >= xTo) {
                throw new RuntimeException("Invalid x coordinates from:to " + xFrom + ':' + xTo);
            } else if (yFrom >= yTo) {
                throw new RuntimeException("Invalid y coordinates from:to " + yFrom + ':' + yTo);
            }
            this.xFrom = xFrom;
            this.xTo = xTo;
            this.yFrom = yFrom;
            this.yTo = yTo;
            this.xLength = (xTo - xFrom) + 1;
            this.yLength = (yTo - yFrom) + 1;
        }

        public int getXFrom() {
            return xFrom;
        }

        public int getXTo() {
            return xTo;
        }

        public int getYFrom() {
            return yFrom;
        }

        public int getYTo() {
            return yTo;
        }

        public int getXLength() {
            return xLength;
        }

        public int getYLength() {
            return yLength;
        }

        public int getArea() {
            return xLength * yLength;
        }
    }
}
