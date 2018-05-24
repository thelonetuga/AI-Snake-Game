package snake;

import java.awt.Color;

public class Cell {
    public static final Color COLOR = Color.WHITE;

    private final int line, column;
    private SnakeAgent agent;
    private Food food;
    private Tail tail;



    public Cell(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public SnakeAgent getAgent() {
        return agent;
    }

    public void setAgent(SnakeAgent agent) {
        this.agent = agent;
    }

    public boolean hasAgent() {
        return agent != null;
    }

    public Food getFood() { return food; }

    public void setFood(Food food) { this.food = food; }

    public boolean hastFood() { return food != null; }


    public boolean hasTail(){
        return tail != null;
    }

    public Tail getTail() {
        return tail;
    }

    public void setTail(Tail tail) {
        this.tail = tail;
    }

    public Color getColor() {
        if (hasAgent()) {
            return agent.getColor();
        } else if (hastFood()) {
            return food.getColor();
        } else if (hasTail()) {
            return tail.getColor();
        }else{
            return Cell.COLOR;
        }
    }
}