package snake.snakeAI;

import snake.Environment;
import snake.SnakeAgent;
import snake.snakeAI.ga.RealVectorIndividual;
import snake.snakeAI.nn.SnakeAIAgent;

import java.awt.*;
import java.util.Random;

public class SnakeIndividual extends RealVectorIndividual<SnakeProblem, SnakeIndividual> {
    private int numFoods;
    private int numMov;

    public SnakeIndividual(SnakeProblem problem, int size, Random random) {
        super(problem, size, random);
        //TODO?
        numFoods =0;
        numMov  = 0;
    }

    public SnakeIndividual(SnakeIndividual original) {
        super(original);
        this.numMov=original.numMov;
        this.numFoods=original.numFoods;
        this.fitness=original.fitness;
    }

    //para cada simulacao (utilizar variavel de iteracao com seed do random)
    //ir ao genoma buscar os pesos das sinapses e colocalos na RN(SetWeights)
    //Mandar a SnakeAI decidir
    //colocar os inputs com os valores percepcionados
    //mandar executar o fowardPropagation
    //observar os valores dos outputs
    //decidir acao
    //manda a cobra iterar o maximo de X vezes(depende do dataSet)
    //recolhe estattisticas(eg, comidas, iteracoes,....)

    //atribuir e devolver a fitness => media(valorizar mais as comidas do que as iteracoes)

    @Override
    public double computeFitness() {

        int enviromentSimulations = problem.getNumEvironmentSimulations();
        Environment environment = problem.getEnvironment();
        SnakeAIAgent aiAgent;
        fitness= 0;
        numFoods= 0;
         numMov=0;
         int[] vetorEstatisticas = new int[]{0,0};

        for (int i = 0; i < enviromentSimulations; i++){
            problem.getEnvironment().initialize(i);
            aiAgent = (SnakeAIAgent) environment.getAgents().get(0);
            aiAgent.setWeights(genome);
            vetorEstatisticas =environment.simulateAI();
            numFoods+= vetorEstatisticas[1];
            numMov += vetorEstatisticas[0];

        }
        fitness=(numFoods *500 + numMov)/enviromentSimulations;
        return fitness;
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
        sb.append((double) numMov/problem.getNumEvironmentSimulations());
        sb.append("\nNumber of Foods: ");
        sb.append((double)numFoods/problem.getNumEvironmentSimulations());
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
