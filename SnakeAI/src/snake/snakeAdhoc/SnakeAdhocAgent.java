package snake.snakeAdhoc;

import snake.*;

import java.awt.*;


public class SnakeAdhocAgent extends SnakeAgent {
    private Environment environment;


    public SnakeAdhocAgent(Cell cell, Color color, Environment environment) {
        super(cell, color, environment);
        this.environment = environment;
    }

    @Override
    public Action decide(Perception perception)  {
        Cell north = perception.getN();
        Cell east = perception.getE();
        Cell south = perception.getS();
        Cell west = perception.getW();
        Food food = perception.getF();


        int lineCell = cell.getLine();
        int columnCell = cell.getColumn();
        int lineFood = food.getCell().getLine();
        int columnFood = food.getCell().getColumn();


        if (north != null  && !north.hasAgent() && !north.hasTail()   && lineCell > lineFood ) {
            return Action.NORTH;
        }
        if (south != null  && !south.hasAgent() && !south.hasTail() && lineCell < lineFood) {
            return Action.SOUTH;
        }
        if (west != null  && !west.hasAgent() && !west.hasTail()  && columnCell > columnFood) {
            return Action.WEST;
        }
        if (east != null  && !east.hasAgent() && !east.hasTail()   && columnCell < columnFood ) {
            return Action.EAST;
        }


        if (north != null && !north.hasTail() && !north.hasAgent()  ) {
            return Action.NORTH;
        } else if (south != null && !south.hasTail() && !south.hasAgent()  ) {
            return Action.SOUTH;
        } else if (east != null && !east.hasTail() && !east.hasAgent()    ) {
            return Action.EAST;
        } else if (west!= null && !west.hasTail() && !west.hasAgent()   ) {
            return Action.WEST;
        }
       return null;
    }
}