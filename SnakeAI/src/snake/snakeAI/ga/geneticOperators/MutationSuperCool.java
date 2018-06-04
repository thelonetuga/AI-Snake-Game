package snake.snakeAI.ga.geneticOperators;

import snake.snakeAI.ga.GeneticAlgorithm;
import snake.snakeAI.ga.RealVectorIndividual;

import java.util.Random;

//PLEASE, MODIFY THE CLASS NAME
public class MutationSuperCool<I extends RealVectorIndividual> extends Mutation<I> {
    public MutationSuperCool(double probability ) {
        super(probability);
    }

    @Override
    public void run(I ind) {
        for (int i = 0; i < ind.getNumGenes(); i++) {
            if (GeneticAlgorithm.random.nextDouble()<probability){
                ind.setGene(i, ind.getGene(i)+ GeneticAlgorithm.random.nextDouble()*2-1); // 2*ind.getGene(i));
            }
        }
    }
    
    @Override
    public String toString(){
        return "Mutation Super Cool (" + probability /* + TODO?*/;
    }
}