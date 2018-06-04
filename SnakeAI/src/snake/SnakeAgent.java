package snake;

import java.awt.Color;
import java.util.LinkedList;

public abstract class SnakeAgent {

    protected Cell cell;
    protected Color color;
    protected Environment environment;
    private LinkedList<Tail> tails;
    private boolean killed;
    protected int numFoods;

    public SnakeAgent(Cell cell, Color color, Environment environment) {
        this.cell = cell;
        if(cell != null){this.cell.setAgent(this);}
        this.color = color;
        this.tails=new LinkedList<>();
        this.environment = environment;
        killed = false;
        numFoods = 0;
    }

    public int getfoodTwoDiferent() {
        return numFoods;
    }

    public void act(Environment environment)  {
        Perception perception = buildPerception(environment);
        Action action = decide(perception);
        execute(action, environment);
    }

    protected Perception buildPerception(Environment environment) {
        return new Perception(environment.getEastCell(cell),
                environment.getNorthCell(cell),
                environment.getSouthCell(cell),
                environment.getWestCell(cell), environment.getFood());
    }

    protected void execute(Action action, Environment environment)
    {
        Cell nextCell = null;

        if (action == Action.NORTH && cell.getLine() != 0) {
            nextCell = environment.getNorthCell(cell);
        } else if (action == Action.SOUTH && cell.getLine() != environment.getNumLines() - 1) {
            nextCell = environment.getSouthCell(cell);
        } else if (action == Action.WEST && cell.getColumn() != 0) {
            nextCell = environment.getWestCell(cell);
        } else if (action == Action.EAST && cell.getColumn() != environment.getNumColumns() - 1) {
            nextCell = environment.getEastCell(cell);
        }

        if (nextCell != null && !nextCell.hasAgent() && !nextCell.hasTail()) {
            if (nextCell.hastFood()) {
                Tail tail = new Tail(cell);
                tails.addFirst(tail);
                cell.setTail(tail);
                nextCell.setFood(null);
                environment.placeFood();
                numFoods++;
            }
            if (!tails.isEmpty()) {
                tails.getLast().getCell().setTail(null);
                tails.removeLast();
                Tail tail = new Tail(cell);
                tails.addFirst(tail);
                cell.setTail(tail);
            }
            setCell(nextCell);
        }else{
            killed = true;
        }
    }

    public LinkedList<Tail> getTails() {
        return tails;
    }

    public boolean isKilled(){
            return killed;
    }

    protected abstract Action decide(Perception perception) ;

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell newCell) {
        if(this.cell != null){this.cell.setAgent(null);}
        this.cell = newCell;
        if(newCell != null){newCell.setAgent(this);}
    }

    public Color getColor() {
        return color;
    }
}
