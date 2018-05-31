package snake.snakeAI.ga.geneticOperators;

import snake.snakeAI.ga.GeneticAlgorithm;
import snake.snakeAI.ga.RealVectorIndividual;

import java.util.Random;

//PLEASE, MODIFY THE CLASS NAME
public class MutationSuperCool<I extends RealVectorIndividual> extends Mutation<I> {
    private Random rand;
   
    public MutationSuperCool(double probability , Random random) {
        super(probability);
        this.rand = random;

    }

    @Override
    public void run(I ind) {
        for (int i = 0; i < ind.getNumGenes(); i++) {
            if (rand.nextDouble()<probability){
                ind.setGene(i, ind.getGene(i) *2); // 2*ind.getGene(i));
            }
        }
    }
    
    @Override
    public String toString(){
        return "Mutation Super Cool (" + probability /* + TODO?*/;
    }
}