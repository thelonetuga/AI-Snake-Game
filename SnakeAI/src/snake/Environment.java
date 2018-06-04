package snake;

import snake.snakeAI.nn.SnakeAIAgent;
import snake.snakeAI.nn.SnakeAIAgent2;
import snake.snakeAdhoc.SnakeAdhocAgent;
import snake.snakeRandom.SnakeRandomAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {

    public static Random random;
    private final Cell[][] grid;

    private final List<SnakeAgent> agents;
    private final int numOutputs;
    private final int numInputs;

    private Food food ;
    private final int maxIterations;
    private final int type;
    private int numFood;
    private int numMov;
    private int numHiddenUnits;

    public Environment(int size, int maxIterations, int type, int numHiddenUnits, int numInputs, int numOutputs) {

        this.maxIterations = maxIterations;
        this.type = type;
        this.numHiddenUnits = numHiddenUnits;
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.grid = new Cell[size][size];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
        this.agents = new ArrayList<>();
        random = new Random();
    }

    public void initialize(int seed) {
        food = null;
        numFood=0;
        agents.clear();
        for (Cell[] line: grid) {
            for (Cell cell: line) {
                cell.setTail(null);
                cell.setFood(null);
                cell.setAgent(null);
            }
        }
        random.setSeed(seed);
        placeAgents();
        placeFood();
    }

    public Food getFood(){
        return food;
    }


    // TODO MODIFY TO PLACE ADHOC OR AI SNAKE AGENTS
    private void placeAgents() {
            switch (type){
                case 0:
                    SnakeAdhocAgent snakeAdhocAgent = new SnakeAdhocAgent(getCell(random.nextInt(grid.length),random.nextInt(grid.length)), Color.GREEN, Environment.this);
                    agents.add(snakeAdhocAgent);
                    break;
                case 1:
                    SnakeRandomAgent snakeRandomAgent = new SnakeRandomAgent(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), Color.BLUE, Environment.this);
                    agents.add(snakeRandomAgent);
                    break;
                case 2:
                    SnakeAIAgent snakeAIAgent = new SnakeAIAgent(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), numInputs,numHiddenUnits,numOutputs,Environment.this);
                    agents.add(snakeAIAgent);
                    break;
                case 3:
                    SnakeAIAgent2 snakeAIAgent2 = new SnakeAIAgent2(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), numInputs,numHiddenUnits,numOutputs,Environment.this);
                    agents.add(snakeAIAgent2);
                    break;
                case 4:
                    SnakeAIAgent snakeEqual = new SnakeAIAgent(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), numInputs,numHiddenUnits,numOutputs,Environment.this);
                    SnakeAIAgent snakeEqual2 = new SnakeAIAgent(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), numInputs,numHiddenUnits,numOutputs,Environment.this);
                    agents.add(snakeEqual);
                    agents.add(snakeEqual2);
                    break;
                case 5:
                    SnakeAIAgent snakeDiferent = new SnakeAIAgent(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), numInputs,numHiddenUnits,numOutputs,Environment.this);
                    SnakeAIAgent2 snakeDiferent2 = new SnakeAIAgent2(getCell(random.nextInt(grid.length), random.nextInt(grid.length)), 13,15,4,Environment.this);
                    agents.add(snakeDiferent);
                    agents.add(snakeDiferent2);
                    break;
            }
    }

    public void placeFood() {
        int linha, coluna;
        do {
            linha = random.nextInt(grid.length);
            coluna = random.nextInt(grid.length);
        }while(grid[linha][coluna].hasAgent() || grid[linha][coluna].hasTail());
        Cell cell = getCell(linha ,coluna);
        food = new Food(cell);
        grid[linha][coluna].setFood(food);
        numFood++;
    }

    public void simulate()  {
        int i;
        numMov=0;
        for ( i = 0; i < maxIterations; i++){
            for (SnakeAgent agent: agents){
                if(!agent.isKilled()) {
                    agent.act(this);
                    fireUpdatedEnvironment();
                }else{
                    break;
                }
            }
        }
        numMov = i;
        fireUpdatedEnvironment();
    }

    public int[] simulateAI() {
        int[] vetorEstatisticas = new int[3];
        for (int i = 0; i < maxIterations; i++){
            for (SnakeAgent agent: agents){
                if(!agent.isKilled()) {
                    agent.act(this);
                    fireUpdatedEnvironment();
                }else{
                    if (type == 5){
                        vetorEstatisticas[0] = i;
                        vetorEstatisticas[1] = getNumFood_1();
                        vetorEstatisticas[2] = getNumFood_2();
                    }else{
                        vetorEstatisticas[0] = i;
                        vetorEstatisticas[1] = getNumFood();
                    }
                    return vetorEstatisticas;
                }
            }
        }
        fireUpdatedEnvironment();
        return vetorEstatisticas;
    }

    public int getNumMovs() {
        return numMov;
    }

    public int getNumFood_1(){
        return agents.get(0).getfoodTwoDiferent();
    }
    public int getNumFood_2(){
        return agents.get(1).getfoodTwoDiferent();
    }

    public List<SnakeAgent> getAgents() {
        return agents;
    }

    public int getSize() {
        return grid.length;
    }

        public Cell getNorthCell(Cell cell) {
        if (cell.getLine() > 0) {
            return grid[cell.getLine() - 1][cell.getColumn()];
        }
        return null;
    }

    public Cell getSouthCell(Cell cell) {
        if (cell.getLine() < grid.length - 1) {
            return grid[cell.getLine() + 1][cell.getColumn()];
        }
        return null;
    }

    public Cell getEastCell(Cell cell) {
        if (cell.getColumn() < grid[0].length - 1) {
            return grid[cell.getLine()][cell.getColumn() + 1];
        }
        return null;
    }

    public Cell getWestCell(Cell cell) {
        if (cell.getColumn() > 0) {
            return grid[cell.getLine()][cell.getColumn() - 1];
        }
        return null;
    }

    public int getNumLines() {
        return grid.length;
    }

    public int getNumColumns() {
        return grid[0].length;
    }

    public final Cell getCell(int linha, int coluna) {
        return grid[linha][coluna];
    }

    public Color getCellColor(int linha, int coluna) {
        return grid[linha][coluna].getColor();
    }

    //listeners
    private final ArrayList<EnvironmentListener> listeners = new ArrayList<>();

    public synchronized void addEnvironmentListener(EnvironmentListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public synchronized void removeEnvironmentListener(EnvironmentListener l) {
        listeners.remove(l);
    }

    public void fireUpdatedEnvironment() {
        for (EnvironmentListener listener : listeners) {
            listener.environmentUpdated();
        }
    }

    public Random getRandom() {
        return random = new Random();
    }

    public int getNumFood() {
        return numFood;
    }

    public int getType() {
        return type;
    }
}
