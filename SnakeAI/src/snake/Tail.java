package snake;

import java.awt.*;

public class Tail {
    private Cell cell;

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Tail(Cell cell) {
        this.cell = cell;
    }

    public Color getColor(){
        return Color.magenta;
    }
}
