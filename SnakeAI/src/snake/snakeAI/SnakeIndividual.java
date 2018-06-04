package snake.snakeAI;

import snake.Environment;
import snake.snakeAI.ga.RealVectorIndividual;
import snake.snakeAI.nn.SnakeAIAgent;
import snake.snakeAI.nn.SnakeAIAgent2;


public class SnakeIndividual extends RealVectorIndividual<SnakeProblem, SnakeIndividual> {
    protected int numFoods;
    protected int numMov;
    protected int numFoods_2;
    private double PESO_MOVIMENTO = 0.5;
    private int PESO_COMIDA = 500;


    public SnakeIndividual(SnakeProblem problem, int size) {
        super(problem, size);
    }

    public SnakeIndividual(SnakeIndividual original) {
        super(original);
        this.numMov=original.numMov;
        this.numFoods=original.numFoods;
        this.numFoods_2=original.numFoods_2;
        this.fitness=original.fitness;
    }

    @Override
    public double computeFitness() {
        int enviromentSimulations = problem.getNumEvironmentSimulations();
        Environment environment = problem.getEnvironment();
        fitness=numFoods=numMov=numFoods_2=0;
        int[] vetorEstatisticas;
        int type = environment.getType();
        if (type == 2){
          for (int i = 0; i < enviromentSimulations; i++) {
              problem.getEnvironment().initialize(i);
              ((SnakeAIAgent)  environment.getAgents().get(0)).setWeights(genome);
              vetorEstatisticas = environment.simulateAI();
              numFoods += vetorEstatisticas[1];
              numMov += vetorEstatisticas[0];
          }
            return     fitness= ((numFoods*PESO_COMIDA + numMov*PESO_MOVIMENTO)/enviromentSimulations);
        } else if (type == 4){
            for (int i = 0; i < enviromentSimulations; i++) {
                problem.getEnvironment().initialize(i);
                ((SnakeAIAgent)  environment.getAgents().get(0)).setWeights(genome);
                ((SnakeAIAgent)  environment.getAgents().get(1)).setWeights(genome);
                vetorEstatisticas = environment.simulateAI();
                numFoods += vetorEstatisticas[1];
                numMov += vetorEstatisticas[0];
            }
            return     fitness= ((numFoods*PESO_COMIDA + numMov*PESO_MOVIMENTO)/enviromentSimulations);
        }else if (type == 3) {
            for (int i = 0; i < enviromentSimulations; i++) {
                problem.getEnvironment().initialize(i);
                ((SnakeAIAgent2) environment.getAgents().get(0)).setWeights(genome);
                vetorEstatisticas = environment.simulateAI();
                numFoods += vetorEstatisticas[1];
                numMov += vetorEstatisticas[0];
            }
            return   fitness = ((numFoods * PESO_COMIDA + numMov * PESO_MOVIMENTO) / enviromentSimulations);
        }else{
            for (int i = 0; i < enviromentSimulations; i++) {
                problem.getEnvironment().initialize(i);
                double[] weights1 = new double[9*10+(10+1)*4];
                double[] weights2 = new double[13*15+(15+1)*8];
                System.arraycopy(genome, 0, weights1,0,weights1.length);
                System.arraycopy(genome, weights1.length, weights2,0,weights2.length);
                ((SnakeAIAgent) environment.getAgents().get(0)).setWeights(weights1);
                ((SnakeAIAgent2) environment.getAgents().get(1)).setWeights(weights2);
                vetorEstatisticas = environment.simulateAI();
                numFoods_2 += environment.getAgents().get(1).getTails().size();
                numFoods += environment.getAgents().get(0).getTails().size();
                numMov += vetorEstatisticas[0];
            }
            return  fitness = ((numFoods + numFoods_2)* PESO_COMIDA + numMov*PESO_MOVIMENTO) - (PESO_COMIDA * Math.abs(numFoods-numFoods_2));
        }
    }

    public double[] getGenome(){
        return genome;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nfitness: ");
        sb.append(fitness);
        sb.append("\nNumber of Movements: ");
        sb.append(numMov/problem.getNumEvironmentSimulations());
        sb.append("\nNumber of Foods : ");
        sb.append(numFoods/problem.getNumEvironmentSimulations());
        sb.append("\nNumber of Foods Snake 2: ");
        sb.append(numFoods_2/problem.getNumEvironmentSimulations());

        return sb.toString();
    }

    /**
     *
     * @param i
     * @return 1 if this object is BETTER than i, -1 if it is WORST than I and
     * 0, otherwise.
     */
    @Override
    public int compareTo(SnakeIndividual i) {
        if (this.fitness > i.fitness)
            return 1;

        if (this.fitness == i.fitness)
            return 0;

        return -1;
    }

    @Override
    public SnakeIndividual clone() {
        return new SnakeIndividual(this);
    }
}
